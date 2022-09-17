package Ozon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/*
D. Отчет (15 баллов)
ограничение по времени на тест2 секунды
ограничение по памяти на тест512 мегабайт
вводстандартный ввод
выводстандартный вывод
Директор IT-корпорации оценивает эффективность работы сотрудников по различным показателям и критериям. Один из этих критериев сформулирован следующим образом: приступив к некоторому заданию, сотрудник должен завершить его, не переключаясь на другие задания.

Чтобы проверить сотрудников на соответствие этому критерию, директор потребовал от каждого сотрудника отчет о том, какие задания он выполнял в последние n дней. Отчет — это последовательность из n целых чисел a1,a2,…,an, где ai — идентификатор задания, которое сотрудник выполнял в i-й день.

Вам необходимо написать программу, проверяющую, соответствует ли сотрудник критерию по его отчету. Сотрудник соответствует этому критерию, если не существует такого задания x, которое выполнялось с перерывом (т. е. в некоторый день i сотрудник выполнял задание x, в дни с i+1 по j−1 он занимался другими заданиями, а в день j сотрудник продолжил выполнение задания x, при этом j>i+1). Иными словами, каждое задание, которое выполнял сотрудник, должно занимать один непрерывный отрезок дней.

Неполные решения этой задачи (например, недостаточно эффективные) могут быть оценены частичным баллом.

Входные данные
В первой строке задано одно целое число t (1≤t≤10) — количество наборов входных данных.

Каждый набор входных данных состоит из двух строк. В первой строке задано одно целое число n (3≤n≤50000). Во второй строке заданы n целых чисел a1,a2,…,an (1≤ai≤n) — отчет сотрудника.

Выходные данные
Для каждого набора входных данных выведите ответ на отдельной строке. Если отчет соответствует критерию, выведите YES, иначе выведите NO.

Пример
входные данные
5
5
1 2 3 4 5
4
1 2 3 1
8
2 3 4 8 5 5 5 5
5
1 1 3 2 2
5
1 1 2 3 2

выходные данные
YES
NO
YES
YES
NO
*/

public class Report {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        var days = Stream.iterate(1, i -> scanner.hasNextLine(), i -> i + 1)
                .map(i -> scanner.nextLine())
                .map(inputLine -> {
                    String[] parts = inputLine.split("\\s+");
                    return Arrays.stream(parts).map(Integer::parseInt).collect(Collectors.toList());
                })
                .collect(Collectors.toList());

        checkReport(days).forEach(System.out::println);
    }

    private static List<String> checkReport(List<List<Integer>> days) {
        return days.stream()
                .skip(1)
                .filter(n -> n.size() > 1)
                .map(Report::RemoveConsecutive)
                .map(s -> {
                    if (s.size() == s.stream().distinct().count()) {
                        return "YES";
                    } else {
                        return "NO";
                    }
                })
                .collect(Collectors.toList());
    }

    private static List<Integer> RemoveConsecutive(List<Integer> list) {
        List<Integer> newList = new ArrayList<>();
        newList.add(list.get(0));

        for (int i = 1; i < list.size(); i++) {
            if (!list.get(i).equals(newList.get(newList.size() - 1))) {
                newList.add(list.get(i));
            }
        }
        return newList;
    }
}
