package Ozon;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/*
I. Рифмы (30 баллов)
ограничение по времени на тест2 секунды
ограничение по памяти на тест512 мегабайт
вводстандартный ввод
выводстандартный вывод
Вы разрабатываете программу автоматической генерации стихотворений. Один из модулей этой программы должен подбирать рифмы к словам из некоторого словаря.

Словарь содержит n различных слов. Словами будем называть последовательности из 1—10 строчных букв латинского алфавита.

Зарифмованность двух слов — это длина их наибольшего общего суффикса (суффиксом будем называть какое-то количество букв в конце слова). Например:

task и flask имеют зарифмованность 3 (наибольший общий суффикс — ask);
decide и code имеют зарифмованность 2 (наибольший общий суффикс — de);
id и void имеют зарифмованность 2 (наибольший общий суффикс — id);
code и forces имеют зарифмованность 0.
Ваша программа должна обработать q запросов следующего вида: дано слово ti (возможно, принадлежащее словарю), необходимо найти слово из словаря, которое не совпадает с ti и имеет максимальную зарифмованность с ti среди всех слов словаря, не совпадающих с ti. Если подходящих слов несколько — выведите любое из них.

Неполные решения этой задачи (например, недостаточно эффективные) могут быть оценены частичным баллом.

Входные данные
Первая строка содержит одно целое число n (2≤n≤50000) — размер словаря.

Далее следуют n строк, i-я строка содержит одну строку si (1≤|si|≤10) — i-е слово из словаря. В словаре все слова различны.

Следующая строка содержит одно целое число q (1≤q≤50000) — количество запросов.

Далее следуют q строк, i-я строка содержит одну строку ti (1≤|ti|≤10) — i-й запрос.

Каждая строка si и каждая строка ti состоит только из строчных букв латинского алфавита.

Выходные данные
Для каждого запроса выведите одну строку — слово из словаря, которое не совпадает с заданным в запросе и имеет с ним максимальную зарифмованность (если таких несколько — выведите любое).

Пример
входные данные
3
task
decide
id
6
flask
code
void
forces
id
ask

выходные данные
task
decide
id
task
decide
task
*/

public class Rhymes {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        var words = Stream.iterate(1, i -> scanner.hasNextLine(), i -> i + 1)
                .map(i -> scanner.nextLine())
                .collect(Collectors.toList());

        findRhymes(words).forEach(System.out::println);
    }

    private static List<String> findRhymes(List<String> words) {
        return calculateFromDict(rearrange(words));
    }

    private static List<List<String>> rearrange(List<String> list) {
        List<List<String>> parsedList = new ArrayList<>();
        var dictSize = Integer.parseInt(list.get(0));

        var dict = IntStream.range(1, dictSize + 1)
                .mapToObj(list::get)
                .map(n -> new StringBuilder(n).reverse().toString())
                .sorted()
                .collect(Collectors.toList());
        var rhymeList = IntStream.range(dictSize + 2, list.size())
                .mapToObj(list::get)
                .map(n -> new StringBuilder(n).reverse().toString())
                .collect(Collectors.toList());
        parsedList.add(dict);
        parsedList.add(rhymeList);
        return parsedList;
    }

    private static List<String> calculateFromDict(List<List<String>> list) {
        var dict = list.get(0);
        var input = list.get(1);

        return input.stream()
                .parallel()
                .map(inputString -> {
                    StringBuilder sb = new StringBuilder();
                    String result = dict.stream()
                            .filter(n -> !n.equals(inputString))
                            .findAny()
                            .get();

                    for (int i = 0; i < inputString.length(); i++) {
                        sb.append(inputString.charAt(i));
                        int subResult = partialStringBinarySearch(dict, sb.toString(), true);
                        int subResultIfEqualInDict = partialStringBinarySearch(dict, sb.toString(), false);

                        if ((subResult < 0 && subResultIfEqualInDict < 0) ||
                                (dict.get(subResult).equals(inputString) && subResultIfEqualInDict < 0) ||
                                (dict.get(subResult).equals(inputString) && subResultIfEqualInDict == subResult)) {
                            break;
                        } else if (dict.get(subResult).equals(inputString) && subResultIfEqualInDict != subResult) {
                            result = dict.get(subResultIfEqualInDict);
                        } else {
                            result = dict.get(subResult);
                        }
                    }
                    return result;
                })
                .map(n -> new StringBuilder(n).reverse().toString())
                .collect(Collectors.toList());
    }

    private static int partialStringBinarySearch(List<String> list, String key, Boolean flag) {
        int low = 0;
        int high = list.size() - 1;
        int result = -1;

        while (low <= high) {
            int mid = (low + high) >>> 1;
            String midVal = list.get(mid);
            int cmp = midVal.substring(0, Math.min(key.length(), midVal.length())).compareTo(key);

            if (cmp < 0)
                low = mid + 1;
            else if (cmp > 0)
                high = mid - 1;
            else {
                result = mid; // key found
                if (flag) high = mid - 1;
                else low = mid + 1;
            }
        }
        return result;
    }
}
