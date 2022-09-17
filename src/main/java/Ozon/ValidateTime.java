package Ozon;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/*
E. Отрезки времени (20 баллов)
ограничение по времени на тест2 секунды
ограничение по памяти на тест512 мегабайт
вводстандартный ввод
выводстандартный вывод
Вам задан набор отрезков времени. Каждый отрезок задан в формате HH:MM:SS-HH:MM:SS, то есть сначала заданы часы, минуты и секунды левой границы отрезка, а затем часы, минуты и секунды правой границы.

Вам необходимо выполнить валидацию заданного набора отрезков времени. Иными словами, вам нужно проверить следующие условия:

часы, минуты и секунды заданы корректно (то есть часы находятся в промежутке от 0 до 23, а минуты и секунды — в промежутке от 0 до 59);
левая граница отрезка находится не позже его правой границы (но границы могут быть равны);
никакая пара отрезков не пересекается (даже в граничных моментах времени).
Вам необходимо вывести YES, если заданный набор отрезков времени проходит валидацию, и NO в противном случае.

Вам необходимо ответить на t независимых наборов тестовых данных.

Неполные решения этой задачи (например, недостаточно эффективные) могут быть оценены частичным баллом.

Входные данные
Первая строка входных данных содержит одно целое число t (1≤t≤10) — количество наборов тестовых данных. Затем следуют t наборов.

Первая строка набора содержит одно целое число n (1≤n≤2⋅104) — количество отрезков времени. В следующих n строках следуют описания отрезков.

Описание отрезка времени задано в формате HH:MM:SS-HH:MM:SS, где HH, MM и SS — последовательности из двух цифр. Заметьте, что никаких пробелов в описании формата нет. Также ни в одном описании нет пробелов в начале и конце строки.

Выходные данные
Для каждого набора тестовых данных выведите ответ — YES, если заданный набор отрезков времени проходит валидацию, и NO в противном случае. Ответы выводите в порядке следования наборов во входных данных.

Пример
входные данные
6
1
02:46:00-03:14:59
2
23:59:59-23:59:59
00:00:00-23:59:58
2
23:59:58-23:59:59
00:00:00-23:59:58
2
23:59:59-23:59:58
00:00:00-23:59:57
6
17:53:39-20:20:02
10:39:17-11:00:52
08:42:47-09:02:14
09:44:26-10:21:41
00:46:17-02:07:19
22:42:50-23:17:46
1
24:00:00-23:59:59

выходные данные
YES
YES
NO
NO
YES
NO
*/

public class ValidateTime {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        var times = Stream.iterate(1, i -> scanner.hasNextLine(), i -> i + 1)
                .map(i -> scanner.nextLine())
                .map(inputLine -> {
                    String[] parts = inputLine.split("-");
                    return Arrays.stream(parts).collect(Collectors.toList());
                })
                .collect(Collectors.toList());

        validate(times).forEach(System.out::println);
    }

    private static List<String> validate(List<List<String>> times) {
        return combineSegments(times).stream()
                .map(ValidateTime::validateNumbers)
                .map(ValidateTime::validateSegments)
                .map(s -> {
                    if (s.contains("NO")) {
                        return "NO";
                    } else {
                        if (1 == s.size()) {
                            return "YES";
                        }

                        var wholeDayInSec = IntStream.range(0, 24 * 60 * 60)
                                .boxed()
                                .collect(Collectors.toCollection(HashSet::new));
                        var isValid = true;
                        for (int i = 0; i < s.size(); i++) {
                            var from = Integer.parseInt(s.get(i).split(":")[0]);
                            var to = Integer.parseInt(s.get(i).split(":")[1]);
                            for (int j = from; j <= to; j++) {
                                var isRemoved = wholeDayInSec.remove(j);
                                if (isRemoved) continue;

                                isValid = false;
                                break;
                            }
                        }
                        return isValid ? "YES" : "NO";
                    }
                })
                .collect(Collectors.toList());
    }

    private static List<List<String>> combineSegments(List<List<String>> strings) {
        List<List<String>> newList = new ArrayList<>();

        for (int i = 1; i < strings.size(); i++) {
            if (1 == strings.get(i).size()) {
                int j = Integer.parseInt(strings.get(i).get(0));
                List<String> subList = new ArrayList<>();
                for (int k = 0; k < j; k++) {
                    subList.add(strings.get(k + i + 1).get(0));
                    subList.add(strings.get(k + i + 1).get(1));
                }
                newList.add(subList);
            }
        }
        return newList;
    }

    private static List<String> validateNumbers(List<String> list) {
        return list.stream()
                .map(n -> {
                    String[] parts = n.split(":");
                    int hours = Integer.parseInt(parts[0]);
                    int minutes = Integer.parseInt(parts[1]);
                    int seconds = Integer.parseInt(parts[2]);

                    if ((0 > hours || 23 < hours) || (0 > minutes || 59 < minutes) || (0 > seconds || 59 < seconds)) {
                        return "NO";
                    }
                    return Integer.toString(hours * 60 * 60 + minutes * 60 + seconds);
                })
                .collect(Collectors.toList());
    }

    private static List<String> validateSegments(List<String> list) {
        return IntStream.range(0, list.size() / 2)
                .mapToObj(n -> {
                    String first = list.get(2 * n);
                    String second = list.get(2 * n + 1);
                    if ((first.equals("NO") || second.equals("NO")) ||
                            (Integer.parseInt(second) < Integer.parseInt(first))) {
                        return "NO";
                    }

                    return String.format("%s:%s", first, second);
                })
                .collect(Collectors.toList());
    }
}
