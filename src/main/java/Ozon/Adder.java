package Ozon;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/*
A. Сумматор (5 баллов)
ограничение по времени на тест3 секунды
ограничение по памяти на тест256 мегабайт
вводстандартный ввод
выводстандартный вывод

Напишите программу, которая выводит сумму двух целых чисел.

Входные данные
В первой строке входных данных содержится целое число t (1≤t≤104) — количество наборов входных данных в тесте.

Далее следуют описания t наборов входных данных, один набор в строке.

В первой (и единственной) строке набора записаны два целых числа a и b (−1000≤a,b≤1000).

Выходные данные
Для каждого набора входных данных выведите сумму двух заданных чисел, то есть a+b.

входные данные
5
256 42
1000 1000
-1000 1000
-1000 1000
20 22

выходные данные
298
2000
0
0
42
*/

public class Adder {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        var numbers = Stream.iterate(1, i -> scanner.hasNextLine(), i -> i + 1)
                .map(i -> scanner.nextLine())
                .map(inputLine -> {
                    String[] parts = inputLine.split("\\s+");
                    if (2 == parts.length) {
                        return Arrays.asList(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
                    } else {
                        return Collections.singletonList(Integer.parseInt(parts[0]));
                    }
                })
                .collect(Collectors.toList());

        sum(numbers).forEach(System.out::println);
    }

    public static List<Integer> sum(List<List<Integer>> numbers) {
        return numbers.stream()
                .filter(n -> n.size() > 1)
                .map(x -> x.stream().reduce(0, Integer::sum))
                .collect(Collectors.toList());
    }
}
