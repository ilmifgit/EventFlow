package ru.eventflow.io;

import ru.eventflow.algorithms.BlockPartition;
import ru.eventflow.algorithms.GraphAlgorithms;
import ru.eventflow.algorithms.Knapsack;
import ru.eventflow.algorithms.SlidingWindow;
import ru.eventflow.algorithms.TwoPointers;
import ru.eventflow.model.Action;
import ru.eventflow.model.Participant;
import ru.eventflow.model.ScheduleTalk;
import ru.eventflow.model.Talk;
import ru.eventflow.services.AnalyticsService;
import ru.eventflow.services.ChangeLogService;
import ru.eventflow.services.ListService;
import ru.eventflow.services.ParticipantRegistry;
import ru.eventflow.services.ScheduleService;
import ru.eventflow.services.VenueService;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

/** Консольное меню EventFlow — все BR-1 … BR-7. */
public class ConsoleMenu {

    private final InputReader input;
    private final ChangeLogService changeLog = new ChangeLogService();
    private final ParticipantRegistry registry = new ParticipantRegistry();
    private final ListService listService = new ListService();
    private final VenueService venue = new VenueService();
    private final ScheduleService scheduleService = new ScheduleService();
    private final AnalyticsService analytics = new AnalyticsService();
    private boolean running = true;

    public ConsoleMenu(InputReader input) { this.input = input; }

    public void run() {
        while (running) {
            printMainMenu();
            int choice = input.readInt("Ваш выбор: ", -1);
            handleMainChoice(choice);
        }
    }

    private void printMainMenu() {
        System.out.println();
        System.out.println("=== EventFlow — сервис организатора конференций ===");
        System.out.println("  1. Журнал изменений программы           (BR-1)");
        System.out.println("  2. Реестр участников                    (BR-2)");
        System.out.println("  3. Списки и поиск                       (BR-3)");
        System.out.println("  4. Схема площадки                       (BR-4)");
        System.out.println("  5. Персональное расписание              (BR-5)");
        System.out.println("  6. Аналитика посещаемости               (BR-6)");
        System.out.println("  7. Блоки программы                      (BR-7)");
        System.out.println("  0. Выход");
    }

    private void handleMainChoice(int choice) {
        switch (choice) {
            case 1 -> showChangeLogMenu();
            case 2 -> showRegistryMenu();
            case 3 -> showListMenu();
            case 4 -> showVenueMenu();
            case 5 -> showScheduleMenu();
            case 6 -> showAnalyticsMenu();
            case 7 -> showBlocksMenu();
            case 0 -> { System.out.println("Выход."); running = false; }
            default -> System.out.println("  Неизвестный пункт.");
        }
    }

    // ============ BR-1 ============
    private void showChangeLogMenu() {
        while (true) {
            System.out.println();
            System.out.println("--- BR-1. Журнал изменений программы ---");
            System.out.println("  Записей в журнале: " + changeLog.size());
            System.out.println("  1. Добавить действие");
            System.out.println("  2. Откатить последнее действие");
            System.out.println("  3. Показать журнал");
            System.out.println("  0. Назад");
            int choice = input.readInt("Ваш выбор: ", 0);
            switch (choice) {
                case 1 -> addActionInteractive();
                case 2 -> {
                    Action u = changeLog.undo();
                    System.out.println(u == null ? "  Журнал пуст — откатывать нечего." : "  Откачено: " + u);
                }
                case 3 -> changeLog.printHistory();
                case 0 -> { return; }
                default -> System.out.println("  Неизвестный пункт.");
            }
        }
    }

    private void addActionInteractive() {
        System.out.println("  1. Добавление доклада");
        System.out.println("  2. Перенос доклада");
        System.out.println("  3. Отмена доклада");
        int t = input.readInt("  Тип: ", 0);
        Action.Type type = switch (t) {
            case 1 -> Action.Type.ADD_TALK;
            case 2 -> Action.Type.MOVE_TALK;
            case 3 -> Action.Type.CANCEL_TALK;
            default -> null;
        };
        if (type == null) { System.out.println("  Неизвестный тип."); return; }
        String d = input.readString("  Описание: ");
        if (d.isEmpty()) { System.out.println("  Пустое описание."); return; }
        System.out.println("  Добавлено: " + changeLog.log(type, d));
    }

    // ============ BR-2 ============
    private void showRegistryMenu() {
        while (true) {
            System.out.println();
            System.out.println("--- BR-2. Реестр участников ---");
            System.out.println("  Участников: " + registry.size());
            System.out.println("  1. Добавить");
            System.out.println("  2. Найти по ID");
            System.out.println("  3. Показать всех (по возрастанию ID)");
            System.out.println("  0. Назад");
            int choice = input.readInt("Ваш выбор: ", 0);
            switch (choice) {
                case 1 -> {
                    int id = input.readInt("  ID (> 0): ", -1);
                    if (id <= 0) { System.out.println("  Некорректный ID."); break; }
                    String name = input.readString("  Имя: ");
                    if (name.isEmpty()) { System.out.println("  Пустое имя."); break; }
                    int exp = Math.max(0, input.readInt("  Опыт (лет): ", 0));
                    boolean isNew = registry.add(new Participant(id, name, exp));
                    System.out.println(isNew ? "  Добавлен." : "  Заменено.");
                }
                case 2 -> {
                    int id = input.readInt("  ID: ", -1);
                    Participant p = id > 0 ? registry.findById(id) : null;
                    System.out.println(p == null ? "  Не найден." : "  " + p);
                }
                case 3 -> registry.printAllSorted();
                case 0 -> { return; }
                default -> System.out.println("  Неизвестный пункт.");
            }
        }
    }

    // ============ BR-3 ============
    private void showListMenu() {
        while (true) {
            System.out.println();
            System.out.println("--- BR-3. Списки и поиск ---");
            System.out.println("  Докладов: " + listService.size());
            System.out.println("  1. Заполнить вручную");
            System.out.println("  2. Сгенерировать N (Random seed)");
            System.out.println("  3. Сортировать и показать");
            System.out.println("  4. Найти в интервале времени");
            System.out.println("  5. Замеры производительности");
            System.out.println("  0. Назад");
            int choice = input.readInt("Ваш выбор: ", 0);
            switch (choice) {
                case 1 -> {
                    int n = input.readInt("  Сколько? ", 0);
                    if (n <= 0) { System.out.println("  Нечего добавлять."); break; }
                    Talk[] cur = listService.getTalks();
                    Talk[] next = new Talk[cur.length + n];
                    System.arraycopy(cur, 0, next, 0, cur.length);
                    for (int i = 0; i < n; i++) {
                        int id = cur.length + i + 1;
                        String title = input.readString("  Название #" + id + ": ");
                        if (title.isEmpty()) title = "Talk-" + id;
                        int h = input.readInt("  Час (0-23): ", 9);
                        if (h < 0 || h > 23) h = 9;
                        int m = input.readInt("  Минута (0-59): ", 0);
                        if (m < 0 || m > 59) m = 0;
                        int d = input.readInt("  Длительность (мин): ", 60);
                        if (d <= 0) d = 60;
                        next[cur.length + i] = new Talk(id, title, h * 60 + m, d);
                    }
                    listService.setTalks(next);
                    System.out.println("  Добавлено. Всего: " + listService.size());
                }
                case 2 -> {
                    int n = input.readInt("  N: ", 1000);
                    if (n <= 0) { System.out.println("  Некорректно."); break; }
                    long seed = input.readLong("  Seed (Enter = 42): ", 42);
                    listService.setTalks(DataGenerator.generateTalks(n, seed));
                    System.out.println("  Сгенерировано " + n + " (seed=" + seed + ").");
                }
                case 3 -> {
                    if (listService.size() == 0) { System.out.println("  Список пуст."); break; }
                    long c = listService.sortByTime();
                    System.out.println("  Отсортировано. Сравнений: " + c);
                    Talk[] arr = listService.getTalks();
                    int limit = Math.min(arr.length, 20);
                    for (int i = 0; i < limit; i++) System.out.println("   " + arr[i]);
                    if (arr.length > limit) System.out.println("   ... всего " + arr.length);
                }
                case 4 -> {
                    if (listService.size() == 0) { System.out.println("  Список пуст."); break; }
                    int fh = input.readInt("  Час с: ", 10);
                    int fm = input.readInt("  Мин с: ", 0);
                    int th = input.readInt("  Час по: ", 12);
                    int tm = input.readInt("  Мин по: ", 0);
                    List<Talk> found = listService.findInInterval(fh * 60 + fm, th * 60 + tm);
                    System.out.println("  Найдено " + found.size() + ":");
                    int limit = Math.min(found.size(), 20);
                    for (int i = 0; i < limit; i++) System.out.println("   " + found.get(i));
                }
                case 5 -> {
                    int[] sizes = { 1000, 5000, 10000, 50000 };
                    for (String line : listService.benchmark(sizes)) System.out.println("  " + line);
                }
                case 0 -> { return; }
                default -> System.out.println("  Неизвестный пункт.");
            }
        }
    }

    // ============ BR-4 ============
    private void showVenueMenu() {
        while (true) {
            System.out.println();
            System.out.println("--- BR-4. Схема площадки ---");
            System.out.println("  Залов: " + venue.getHalls().size()
                    + ", рёбер: " + venue.getGraph().edgeCount()
                    + ", вход: " + (venue.getEntryHall() > 0 ? venue.describeHall(venue.getEntryHall()) : "не задан"));
            System.out.println("  1. Загрузить демо-граф");
            System.out.println("  2. Добавить переход");
            System.out.println("  3. BFS от входа");
            System.out.println("  4. DFS от входа");
            System.out.println("  5. Циклы есть?");
            System.out.println("  6. Число изолированных групп");
            System.out.println("  7. Кратчайшие расстояния от входа (Дейкстра)");
            System.out.println("  0. Назад");
            int choice = input.readInt("Ваш выбор: ", 0);
            switch (choice) {
                case 1 -> { venue.loadDemo(); System.out.println("  Демо-граф загружен."); }
                case 2 -> {
                    int f = input.readInt("  Из зала: ", -1);
                    int t = input.readInt("  В зал: ", -1);
                    int w = input.readInt("  Время (мин): ", 1);
                    if (f < 0 || t < 0 || w < 0) { System.out.println("  Некорректные данные."); break; }
                    venue.addTransition(f, t, w);
                    System.out.println("  Добавлено.");
                }
                case 3 -> {
                    if (venue.getEntryHall() <= 0) { System.out.println("  Вход не задан."); break; }
                    List<Integer> order = GraphAlgorithms.bfs(venue.getGraph(), venue.getEntryHall());
                    System.out.println("  Порядок BFS: " + order);
                }
                case 4 -> {
                    if (venue.getEntryHall() <= 0) { System.out.println("  Вход не задан."); break; }
                    System.out.println("  Порядок DFS: " + GraphAlgorithms.dfs(venue.getGraph(), venue.getEntryHall()));
                }
                case 5 -> System.out.println("  Циклы: "
                        + (GraphAlgorithms.hasCycle(venue.getGraph()) ? "есть" : "нет"));
                case 6 -> System.out.println("  Изолированных групп (компонент): "
                        + GraphAlgorithms.weakComponentCount(venue.getGraph()));
                case 7 -> {
                    if (venue.getEntryHall() <= 0) { System.out.println("  Вход не задан."); break; }
                    Map<Integer, Integer> d = venue.shortestFromEntry();
                    for (Map.Entry<Integer, Integer> e : d.entrySet()) {
                        int v = e.getValue();
                        System.out.println("   " + venue.describeHall(e.getKey()) + " — "
                                + (v == Integer.MAX_VALUE ? "недостижим" : v + " мин"));
                    }
                }
                case 0 -> { return; }
                default -> System.out.println("  Неизвестный пункт.");
            }
        }
    }

    // ============ BR-5 ============
    private void showScheduleMenu() {
        while (true) {
            System.out.println();
            System.out.println("--- BR-5. Персональное расписание ---");
            System.out.println("  Докладов: " + scheduleService.size());
            System.out.println("  1. Загрузить демо-набор");
            System.out.println("  2. Сгенерировать N докладов");
            System.out.println("  3. Показать все доклады");
            System.out.println("  4. Рюкзак: оптимальный (DP) и быстрый (жадный)");
            System.out.println("  5. Максимум докладов без пересечений (жадный)");
            System.out.println("  0. Назад");
            int choice = input.readInt("Ваш выбор: ", 0);
            switch (choice) {
                case 1 -> { scheduleService.loadDemo(); System.out.println("  Демо-набор загружен."); }
                case 2 -> {
                    int n = input.readInt("  N: ", 6);
                    if (n <= 0) { System.out.println("  Некорректно."); break; }
                    long seed = input.readLong("  Seed (Enter = 42): ", 42);
                    scheduleService.loadRandom(n, seed);
                    System.out.println("  Сгенерировано.");
                }
                case 3 -> {
                    if (scheduleService.size() == 0) { System.out.println("  Пусто."); break; }
                    for (ScheduleTalk t : scheduleService.getTalks()) System.out.println("   " + t);
                }
                case 4 -> {
                    if (scheduleService.size() == 0) { System.out.println("  Сначала загрузите данные."); break; }
                    int limit = input.readInt("  Лимит времени (мин): ", 240);
                    if (limit <= 0) { System.out.println("  Некорректно."); break; }
                    long t1 = System.nanoTime();
                    Knapsack.Result opt = scheduleService.solveKnapsackOptimal(limit);
                    long t2 = System.nanoTime();
                    long t3 = System.nanoTime();
                    Knapsack.Result fast = scheduleService.solveKnapsackFast(limit);
                    long t4 = System.nanoTime();
                    System.out.println("  Оптимальный (DP):      интерес = " + opt.totalInterest
                            + ", длительность = " + opt.totalDuration + " мин, "
                            + (t2 - t1) / 1_000_000.0 + " мс");
                    System.out.println("   Быстрый (жадный):    интерес = " + fast.totalInterest
                            + ", длительность = " + fast.totalDuration + " мин, "
                            + (t4 - t3) / 1_000_000.0 + " мс");
                    if (opt.totalInterest != fast.totalInterest) {
                        System.out.println("  -> Результаты расходятся: DP даёт больше интереса.");
                    } else {
                        System.out.println("  -> На этом наборе результаты совпали.");
                    }
                }
                case 5 -> {
                    if (scheduleService.size() == 0) { System.out.println("  Сначала загрузите данные."); break; }
                    List<ScheduleTalk> sel = scheduleService.selectNonOverlapping();
                    System.out.println("  Выбрано " + sel.size() + " докладов без пересечений:");
                    for (ScheduleTalk t : sel) System.out.println("   " + t);
                }
                case 0 -> { return; }
                default -> System.out.println("  Неизвестный пункт.");
            }
        }
    }

    // ============ BR-6 ============
    private void showAnalyticsMenu() {
        while (true) {
            System.out.println();
            System.out.println("--- BR-6. Аналитика посещаемости ---");
            System.out.println("  Точек по минутам: " + analytics.sizePerMinute()
                    + ", участников: " + analytics.sizeExperience());
            System.out.println("  1. Сгенерировать поток по минутам");
            System.out.println("  2. Сгенерировать список опыта участников");
            System.out.println("  3. Пик нагрузки: k минут подряд");
            System.out.println("  4. Пара с суммой опыта, ближайшей к target");
            System.out.println("  0. Назад");
            int choice = input.readInt("Ваш выбор: ", 0);
            switch (choice) {
                case 1 -> {
                    int n = input.readInt("  Сколько минут: ", 10000);
                    if (n <= 0) { System.out.println("  Некорректно."); break; }
                    long seed = input.readLong("  Seed (Enter = 42): ", 42);
                    analytics.generatePerMinute(n, seed);
                    System.out.println("  Сгенерировано " + n + " точек.");
                }
                case 2 -> {
                    int n = input.readInt("  Сколько участников: ", 1000);
                    if (n < 2) { System.out.println("  Нужно минимум 2."); break; }
                    long seed = input.readLong("  Seed (Enter = 42): ", 42);
                    analytics.generateExperience(n, seed);
                    System.out.println("  Сгенерировано " + n + " участников.");
                }
                case 3 -> {
                    if (analytics.sizePerMinute() == 0) { System.out.println("  Сначала сгенерируйте поток."); break; }
                    int k = input.readInt("  k (минут): ", 60);
                    if (k <= 0 || k > analytics.sizePerMinute()) { System.out.println("  Некорректное k."); break; }
                    SlidingWindow.Result r = analytics.peakWindow(k);
                    System.out.println("  Пик: сумма = " + r.sum + " (начиная с индекса " + r.startIndex + ")");
                }
                case 4 -> {
                    if (analytics.sizeExperience() < 2) { System.out.println("  Сначала сгенерируйте участников."); break; }
                    long target = input.readLong("  Целевая сумма опыта: ", 40);
                    TwoPointers.Pair p = analytics.closestExperiencePair(target);
                    System.out.println("  Пара: индексы " + p.i + " и " + p.j
                            + ", сумма = " + p.sum + ", |разница| = " + p.diff);
                }
                case 0 -> { return; }
                default -> System.out.println("  Неизвестный пункт.");
            }
        }
    }

    // ============ BR-7 ============
    private void showBlocksMenu() {
        while (true) {
            System.out.println();
            System.out.println("--- BR-7. Блоки программы ---");
            System.out.println("  1. Число разбиений (DP, мгновенно)");
            System.out.println("  2. Число разбиений (наивная рекурсия)");
            System.out.println("  3. Сравнить время на N = 20..30");
            System.out.println("  0. Назад");
            int choice = input.readInt("Ваш выбор: ", 0);
            switch (choice) {
                case 1 -> {
                    int n = input.readInt("  N (0..60): ", 60);
                    if (n < 0 || n > 60) { System.out.println("  Допустимо 0..60."); break; }
                    BigInteger v = BlockPartition.iterative(n);
                    System.out.println("  f(" + n + ") = " + v);
                }
                case 2 -> {
                    int n = input.readInt("  N (0..30, иначе долго): ", 25);
                    if (n < 0 || n > 30) { System.out.println("  Допустимо 0..30."); break; }
                    long t1 = System.nanoTime();
                    BigInteger v = BlockPartition.naive(n);
                    long t2 = System.nanoTime();
                    System.out.println("  f(" + n + ") = " + v
                            + "  за " + (t2 - t1) / 1_000_000.0 + " мс (наивная рекурсия)");
                }
                case 3 -> {
                    System.out.printf("  %-6s | %-16s | %-16s | %-16s%n",
                            "N", "Naive, мс", "Memo, мс", "DP, мс");
                    System.out.println("  " + "-".repeat(62));
                    for (int n : new int[]{20, 22, 24, 26, 28, 30}) {
                        long a = System.nanoTime();
                        BlockPartition.naive(n);
                        long b = System.nanoTime();
                        long c = System.nanoTime();
                        BlockPartition.memoized(n);
                        long d = System.nanoTime();
                        long e = System.nanoTime();
                        BlockPartition.iterative(n);
                        long f = System.nanoTime();
                        System.out.printf("  %-6d | %-16.2f | %-16.4f | %-16.4f%n",
                                n,
                                (b - a) / 1_000_000.0,
                                (d - c) / 1_000_000.0,
                                (f - e) / 1_000_000.0);
                    }
                    System.out.println("  Для N = 60:");
                    long g = System.nanoTime();
                    BigInteger v = BlockPartition.iterative(60);
                    long h = System.nanoTime();
                    System.out.println("   DP мгновенно: f(60) = " + v
                            + " за " + (h - g) / 1_000_000.0 + " мс");
                }
                case 0 -> { return; }
                default -> System.out.println("  Неизвестный пункт.");
            }
        }
    }
}