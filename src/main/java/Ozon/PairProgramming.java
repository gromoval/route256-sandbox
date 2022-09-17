package Ozon;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/*
C. Парное программирование (10 баллов)
ограничение по времени на тест1 секунда
ограничение по памяти на тест512 мегабайт
вводстандартный ввод
выводстандартный вывод
В компании работает n разработчиков, где n — четное число. Сумасшедший менеджер решил разбить всех разработчиков на команды по два человека.

Для этого он составил список всех разработчиков и назначил каждому из них номер по списку (от 1 до n) и значение ai — уровень мастерства i-го в списке разработчика.

Очередную команду он составляет следующим образом:

первый разработчик в команде тот, кто идет первым в списке;
ему в пару подбирается такой, что разница их уровней минимальна (то есть минимально значение |ai−aj|, где |x| — это модуль числа x); если таких кандидатов несколько, то выбирается из них тот, кто находится раньше в списке;
эти два разработчика образуют команду и удаляются из списка.
Например, если массив a равен [2,1,3,1,1,4], то формирование команд будет происходить следующим образом:

назначим разработчикам номера [1,2,3,4,5,6] в соответствии с их положением в списке, первый среди них имеет номер 1, его уровень мастерства a1=2, подходящими (с минимальной абсолютной разностью) являются разработчики с номерами 2,3,4,5, первый среди них 2, таким образом первая команда — это разработчики с номерами 1 и 2;
оставшиеся разработчики теперь имеют номера [3,4,5,6], первый среди них 3, его уровень a3=3, разработчик с минимальной абсолютной разностью только один (номер 6), таким образом команда — разработчики с номерами 3 и 6;
оставшиеся разработчики имеют номера [4,5], первый среди них 4, его уровень a4=1, остался только разработчик с номером 5, таким образом третья команда — разработчики с номерами 4 и 5.
Ваша задача — помочь сумасшедшему менеджеру промоделировать процесс разбиения на команды. Обратите внимание, что команды должны быть выведены в порядке, описанном выше в условии.

Входные данные
Первая строка содержит одно целое число t (1≤t≤50) — количество наборов входных данных.

Первая строка каждого набора содержит одно целое число n (2≤n≤50; n четное) — количество разработчиков.

Вторая строка содержит n целых чисел a1,a2,…,an (1≤ai≤100), где ai — уровень мастерства i-го разработчика.

Выходные данные
Для каждого набора входных данных выведите n2 строк, i-я строка должна содержать пару чисел — номер первого и второго разработчика в i-й команде в порядке, описанном в условии.

Выводите пустую строку между выводами для наборов входных данных.

Пример
входные данные
3
6
2 1 3 1 1 4
2
5 5
8
1 4 2 5 4 2 6 3

выходные данные
1 2
3 6
4 5

1 2

1 3
2 5
4 7
6 8
*/

public class PairProgramming {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        var developers = Stream.iterate(1, i -> scanner.hasNextLine(), i -> i + 1)
                .map(i -> scanner.nextLine())
                .map(inputLine -> {
                    String[] parts = inputLine.split("\\s+");
                    return Arrays.stream(parts).map(Integer::parseInt).collect(Collectors.toList());
                })
                .collect(Collectors.toList());

        getPair(developers).stream()
                .peek(z -> System.out.println(" "))
                .forEach(x -> x.forEach(y -> System.out.println((y.get(0) + 1) + " " + (y.get(1) + 1))
                ));
    }

    private static List<List<List<Integer>>> getPair(List<List<Integer>> developers) {
        return developers.stream()
                .skip(1)
                .filter(n -> n.size() > 1)
                .map(listToMap -> IntStream.range(0, listToMap.size())
                        .boxed()
                        .collect(Collectors.toMap(Function.identity(), listToMap::get)))
                .map(s -> {
                    List<List<Integer>> result = new ArrayList<>();

                    while (2 <= s.size()) {
                        int firstElement = s.keySet().stream().findFirst().get();
                        final int secondElement = getSmallestDiffKey(s, firstElement);

                        var subResult = Arrays.asList(firstElement, secondElement);
                        s.remove(firstElement);
                        s.remove(secondElement);
                        result.add(subResult);
                    }
                    return result;
                })
                .collect(Collectors.toList());
    }

    private static int getSmallestDiffKey(Map<Integer, Integer> map, Integer constantKey) {
        int constant = map.get(constantKey);

        return map.entrySet().stream()
                .filter(entry -> !constantKey.equals(entry.getKey()))
                .map(entry -> new AbstractMap.SimpleEntry<>(
                        entry.getKey(),
                        Math.abs(entry.getValue() - constant)))
                .min(Comparator.comparingInt(AbstractMap.SimpleEntry::getValue))
                .map(Map.Entry::getKey)
                .get();
    }
}
