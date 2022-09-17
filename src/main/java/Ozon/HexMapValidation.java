package Ozon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/*
G. Валидация карты (25 баллов)
ограничение по времени на тест1 секунда
ограничение по памяти на тест512 мегабайт
вводстандартный ввод
выводстандартный вывод
В этой задаче вам необходимо реализовать валидацию корректности карты для стратегической компьютерной игры.

Карта состоит из гексагонов (шестиугольников), каждый из которых принадлежит какому-то региону карты. В файлах игры карта представлена как n строк по m символов в каждой (строки и символы в них нумеруются с единицы). Каждый нечетный символ каждой четной строки и каждый четный символ каждой нечетной строки — точка (символ . с ASCII кодом 46); все остальные символы соответствуют гексагонам и являются заглавными буквами латинского алфавита. Буква указывает на то, какому региону принадлежит гексагон.

Посмотрите на картинку ниже, чтобы понять, как описание карты в файлах игры соответствует карте из шестиугольников.

Соответствие описания карты в файле (слева) и самой карты (справа). Регионы R, G, V, Y и B окрашены в красный, зеленый, фиолетовый, желтый и синий цвет, соответственно.
Вы должны проверить, что каждый регион карты является одной связной областью. Иными словами, не должно быть двух гексагонов, принадлежащих одному и тому же региону, которые не соединены другими гексагонами этого же региона.

Карта слева является корректной. Карта справа не является корректной, так как гексагоны, обозначенные цифрами 1 и 2, принадлежат одному и тому же региону (обозначенному красным цветом), но не соединены другими гексагонами этого региона.
Неполные решения этой задачи (например, недостаточно эффективные) могут быть оценены частичным баллом.

Входные данные
В первой строке задано одно целое число t (1≤t≤100) — количество наборов входных данных.

Первая строка набора входных данных содержит два целых числа n и m (2≤n,m≤20) — количество строк и количество символов в каждой строке в описании карты.

Далее следуют n строк по m символов в каждой — описание карты. Каждый нечетный символ каждой четной строки и каждый четный символ каждой нечетной строки — точка (символ . с ASCII кодом 46); все остальные символы соответствуют гексагонам и являются заглавными буквами латинского алфавита.

Выходные данные
На каждый набор входных данных выведите ответ в отдельной строке — YES, если каждый регион карты представляет связную область, или NO, если это не так.

Пример
входные данные
3
3 7
R.R.R.G
.Y.G.G.
B.Y.V.V
4 8
Y.R.B.B.
.R.R.B.V
B.R.B.R.
.B.B.R.R
2 7
G.B.R.G
.G.G.G.

выходные данные
YES
NO
YES

Примечание
Первые два набора входных данных из примера показаны на второй картинке в условии.
*/

public class HexMapValidation {
    private static String[][] commonMap;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        var input = Stream.iterate(1, i -> scanner.hasNextLine(), i -> i + 1)
                .map(i -> scanner.nextLine())
                .collect(Collectors.toList());

        mapValidate(input).forEach(System.out::println);
    }

    private static List<String> mapValidate(List<String> input) {
        var mapCount = Integer.parseInt(input.get(0));
        var filteredInput = input.stream().skip(1).collect(Collectors.toList());
        List<String[][]> mappedInput = new ArrayList<>();

        int from = 0;
        for (int i = 0; i < mapCount; i++) {
            var to = Integer.parseInt(Arrays.stream(filteredInput.get(from).split("\\s+")).findFirst().get());
            var length = filteredInput.get(from + 1).replaceAll("\\.", "").length();
            mappedInput.add(IntStream.rangeClosed(from + 1, from + to)
                    .parallel()
                    .mapToObj(n -> {
                        var subResult = filteredInput.get(n).replaceAll("\\.", "");
                        if (subResult.length() < length)
                            return String.format("%s0", subResult).split("");
                        return subResult.split("");
                    })
                    .toArray(String[][]::new));
            from += to + 1;
        }

        List<String> result = new ArrayList<>();
        for (String[][] map : mappedInput) {
            List<String> chars = Arrays.stream(map).flatMap(Arrays::stream).distinct().collect(Collectors.toList());
            if (chars.contains("0")) chars.remove("0");

            boolean isValid = true;
            for (String charToCalc : chars) {
                int areas = 0;
                commonMap = Arrays.stream(map)
                        .map(String[]::clone)
                        .map(s -> Arrays.stream(s)
                                .map(n -> {
                                    if (n.equals(charToCalc)) return n;
                                    return "0";
                                })
                                .toArray(String[]::new))
                        .toArray(String[][]::new);

                for (int x = 0; x < commonMap[0].length; x++) {
                    for (int y = 0; y < commonMap.length; y++) {
                        if (commonMap[y][x].equals(charToCalc))
                            if (blank(x, y) > 0)
                                areas++;
                    }
                }
                if (areas > 1) {
                    isValid = false;
                    break;
                }
            }
            if (isValid) result.add("YES");
            else result.add("NO");
        }
        return result;
    }

    private static int blank(int x, int y) {
        if ((x < 0) || (x >= commonMap[0].length) || (y < 0) || (y >= commonMap.length) || commonMap[y][x].equals("0"))
            return 0;

        int coef;
        if (y % 2 == 0) coef = x - 1;
        else coef = x;

        commonMap[y][x] = "0";

        return 1 +
                blank(x - 1, y) +
                blank(x + 1, y) +
                blank(coef, y - 1) +
                blank(coef + 1, y - 1) +
                blank(coef, y + 1) +
                blank(coef + 1, y + 1);
    }
}
