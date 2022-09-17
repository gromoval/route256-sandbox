package Ozon;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/*
B. Сумма к оплате (10 баллов)
ограничение по времени на тест1 секунда
ограничение по памяти на тест256 мегабайт
вводстандартный ввод
выводстандартный вывод

В магазине акция: «купи три одинаковых товара и заплати только за два». Конечно, каждый купленный товар может участвовать лишь в одной акции. Акцию можно использовать многократно.

Например, если будут куплены 7 товаров одного вида по цене 2 за штуку и 5 товаров другого вида по цене 3 за штуку, то вместо 7⋅2+5⋅3 надо будет оплатить 5⋅2+4⋅3=22.

Считая, что одинаковые цены имеют только одинаковые товары, найдите сумму к оплате.

Неполные решения этой задачи (например, недостаточно эффективные) могут быть оценены частичным баллом.

Входные данные
В первой строке записано целое число t (1≤t≤104) — количество наборов входных данных.

Далее записаны наборы входных данных. Каждый начинается строкой, которая содержит n (1≤n≤2⋅105) — количество купленных товаров. Следующая строка содержит их цены p1,p2,…,pn (1≤pi≤104). Если цены двух товаров одинаковые, то надо считать, что это один и тот товар.

Гарантируется, что сумма значений n по всем тестам не превосходит 2⋅105.

Выходные данные
Выведите t целых чисел — суммы к оплате для каждого из наборов входных данных.

Пример
входные данные
6
12
2 2 2 2 2 2 2 3 3 3 3 3
12
2 3 2 3 2 2 3 2 3 2 2 3
1
10000
9
1 2 3 1 2 3 1 2 3
6
10000 10000 10000 10000 10000 10000
6
300 100 200 300 200 300

выходные данные
22
22
10000
12
40000
1100
*/

public class PaymentSumBySale {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        var numbers = Stream.iterate(1, i -> scanner.hasNextLine(), i -> i + 1)
                .map(i -> scanner.nextLine())
                .map(inputLine -> {
                    String[] parts = inputLine.split("\\s+");
                    return Arrays.stream(parts).map(Integer::parseInt).collect(Collectors.toList());
                })
                .collect(Collectors.toList());

        calculateTotal(numbers).forEach(System.out::println);
    }

    public static List<Long> calculateTotal(List<List<Integer>> numbers) {
        final int ACTIONAMOUNT = 3;

        var filtered = IntStream.range(1, numbers.size())
                .filter(n -> n % 2 == 0)
                .mapToObj(numbers::get)
                .collect(Collectors.toList());

        return filtered.stream()
                .map(s -> s.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting())))
                .map(aggregate -> aggregate.entrySet().stream()
                        .map(x -> x.getKey() * (x.getValue() - x.getValue() / ACTIONAMOUNT))
                        .collect(Collectors.toList()))
                .map(y -> y.stream().reduce(0L, Long::sum))
                .collect(Collectors.toList());
    }
}
