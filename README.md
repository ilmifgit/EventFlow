# EventFlow — сервис организатора конференций

Итоговый проект по курсу «Практикум по алгоритмам», вариант 5.

## Задача

Консольный Java-сервис для организаторов конференций. Закрывает 7 бизнес-требований (BR-1 … BR-7): журнал с откатом, реестр участников, сортировки и поиск, схема площадки, персональное расписание, аналитика, разбиение программы на блоки.

## Запуск

Требования: JDK 17+, Maven 3.8+.

    mvn clean compile
    mvn exec:java
    mvn test

Без Maven:

    java -cp target\classes ru.eventflow.Main

## Структура

    src/main/java/ru/eventflow/
      model/        Action, Participant, Talk, ScheduleTalk, Hall
      structures/   MyLinkedList, MyStack, MyBinarySearchTree, MyGraph
      algorithms/   Sortings, BinarySearch, GraphAlgorithms, Knapsack,
                    IntervalScheduling, SlidingWindow, TwoPointers, BlockPartition
      services/     ChangeLogService, ParticipantRegistry, ListService,
                    VenueService, ScheduleService, AnalyticsService
      io/           InputReader, DataGenerator, ConsoleMenu
      Main.java

## Таблица сложностей

| Класс / метод                     | Операция            | Время              | Память    |
|-----------------------------------|---------------------|--------------------|-----------|
| MyLinkedList.addFirst/removeFirst | вставка/удаление    | O(1)               | O(1)      |
| MyStack.push/pop                  | стек LIFO           | O(1)               | O(1)      |
| MyBinarySearchTree.insert/search  | вставка/поиск       | O(log n) ср., O(n) худш. | O(1) |
| MyBinarySearchTree.inOrder        | вывод по возрастанию| O(n)               | O(h)      |
| Sortings.insertionSort            | сортировка          | O(n^2), O(n) почти | O(1)      |
| Sortings.mergeSort                | сортировка          | O(n log n)         | O(n)      |
| BinarySearch.lowerBound/upperBound| границы             | O(log n)           | O(1)      |
| MyGraph                           | хранение            | —                  | O(V + E)  |
| GraphAlgorithms.bfs/dfs           | обход               | O(V + E)           | O(V)      |
| GraphAlgorithms.hasCycle          | поиск цикла         | O(V + E)           | O(V)      |
| GraphAlgorithms.weakComponentCount| компоненты          | O(V + E)           | O(V + E)  |
| GraphAlgorithms.dijkstra          | кратчайшие пути     | O((V+E) log V)     | O(V + E)  |
| Knapsack.solveOptimal             | 0/1 рюкзак          | O(n*W)             | O(n*W)    |
| Knapsack.solveFast                | жадный              | O(n log n)         | O(n)      |
| IntervalScheduling.selectMax      | непересекающиеся    | O(n log n)         | O(n)      |
| SlidingWindow.maxSumK             | окно k              | O(n)               | O(1)      |
| TwoPointers.closestPair           | пара с суммой       | O(n log n)         | O(n)      |
| BlockPartition.naive              | разбиения           | O(3^n)             | O(n)      |
| BlockPartition.memoized           | разбиения           | O(n)               | O(n)      |
| BlockPartition.iterative          | разбиения           | O(n)               | O(1)      |

## Обоснование архитектурных решений

**Связный список для журнала (BR-1).** Массив требует переаллокации при переполнении — O(n) на копирование в момент пиковой нагрузки. Связный список даёт гарантированный O(1) на push/pop. Запрещённые LinkedList, Stack, Deque не используются.

**BST, а не хеш-таблица (BR-2).** Хеш даёт O(1) поиск, но не даёт упорядоченный обход. BST решает обе задачи: поиск O(log n) и симметричный обход за O(n) — сразу отсортированный вывод. Отклонённая альтернатива — AVL: сложнее, но защищает от вырождения в список при подаче ключей по возрастанию.

**Слияние, а не quicksort (BR-3).** Слияние даёт гарантированное O(n log n) и стабильность. Quicksort в среднем быстрее, но в худшем (отсортированные данные, плохой pivot) — O(n^2). Для выгрузок важна предсказуемость.

**Список смежности, а не матрица (BR-4).** Граф площадки разреженный: залов десятки, переходов — единицы на зал. Матрица — O(V^2) памяти. Список смежности — O(V+E). Обходы и Дейкстра на списках дают ту же асимптотику.

**DP для рюкзака, жадный для интервалов (BR-5).** Жадный по отношению интерес/длительность для рюкзака неоптимален (контрпример: лимит 4ч, A=3ч/100 vs B1+B2=2ч+2ч/60+55 — жадный берёт A=100, DP берёт 115). Interval scheduling жадным по времени окончания оптимален: доказывается аргументом обмена.

**Скользящее окно и два указателя (BR-6).** Наивный пересчёт окна — O(n*k). Скользящее окно — O(n). Перебор пар — O(n^2), два указателя после сортировки — O(n log n) и находят ближайшую сумму, а не только точное совпадение.

**Три реализации BR-7.** Наивная рекурсия O(3^n) — на n=30 это миллионы вызовов. Мемоизация убирает повторы → O(n). Итеративный DP → O(n) времени, O(1) памяти. BigInteger, так как f(60) близко к границе long.

## Ограничения

- BST в BR-2 не сбалансирована: при подаче ключей по возрастанию вырождается в список, O(n) на операцию.
- Дейкстра в BR-4 не работает с отрицательными весами — нужен Беллман-Форд.
- Рюкзак O(n*W) плохо масштабируется при больших W — нужны meet-in-the-middle или приближённые схемы.
- Данные хранятся в памяти и не сохраняются между запусками.
