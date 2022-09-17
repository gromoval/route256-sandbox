package Ozon;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/*
F. Возможные друзья (20 баллов)
ограничение по времени на тест3 секунды
ограничение по памяти на тест512 мегабайт
вводстандартный ввод
выводстандартный вывод
Во многих социальных сетях у пользователей есть возможность указать других пользователей как своих друзей. Помимо этого, часто существует система рекомендации друзей, которая показывает пользователям людей, с которыми они знакомы косвенно (через кого-то из своих друзей), и предлагает добавить этих людей в список друзей. Вам предстоит разработать систему рекомендации друзей.

В интересующей нас социальной сети n пользователей, каждому из которых присвоен уникальный id от 1 до n. У каждого пользователя этой сети не более 5 друзей. Очевидно, ни один пользователь не является другом самому себе, и если пользователь x в списке друзей у пользователя y, то и пользователь y входит в список друзей пользователя x.

Опишем, как должен формироваться список возможных друзей для каждого пользователя. Для пользователя x в список должны входить такие пользователи y, что:

y не является другом x и не совпадает с x;
у пользователя y и у пользователя x есть хотя бы один общий друг;
не существует такого пользователя y′, который удовлетворяет первым двум ограничениям, и у которого строго больше общих друзей с x, чем у y с x.
Иными словами, в список возможных друзей пользователя x входят все такие пользователи, не являющиеся его друзьями, для которых количество общих друзей с x максимально. Обратите внимание, что список возможных друзей может быть пустым.

Вы должны написать программу, которая по заданной структуре социальной сети формирует списки возможных друзей для всех пользователей сети.

Неполные решения этой задачи (например, недостаточно эффективные) могут быть оценены частичным баллом.

Входные данные
В первой строке заданы два целых числа n и m (2≤n≤50000; 0≤m≤min(n(n−1)2,5n2)) — количество пользователей и количество пар друзей, соответственно.

Далее следуют m строк, в каждой из которых заданы два целых числа xi и yi (1≤xi,yi≤n; xi≠yi) — очередная пара друзей в социальной сети. Каждая пара друзей задается не более одного раза; у каждого пользователя не более 5 друзей.

Выходные данные
Для каждого пользователя от 1 до n выведите в отдельной строке список его возможных друзей в следующем формате:

если список возможных друзей пуст, выведите одно целое число 0;
иначе выведите id возможных друзей пользователя в возрастающем порядке.

Примеры
входные данные
8 6
4 3
3 1
1 2
2 4
2 5
6 8

выходные данные
4
3
2
1
1 4
0
0
0

входные данные
8 10
1 2
1 3
1 4
4 3
3 2
2 4
1 8
5 6
7 6
5 7

выходные данные
0
8
8
8
0
0
0
2 3 4

Примечание
Рассмотрим первый пример из условия.

Для начала сформируем списки друзей всех пользователей:

друзья пользователя 1: [2,3].
друзья пользователя 2: [1,4,5].
друзья пользователя 3: [1,4].
друзья пользователя 4: [2,3].
друзья пользователя 5: [2].
друзья пользователя 6: [8].
друзья пользователя 7: [] (список друзей пуст).
друзья пользователя 8: [6].
Рассмотрим, как формируются списки возможных друзей для некоторых пользователей.

У пользователя 1 есть два пользователя, которые не являются его друзьями и с которыми у него есть хотя бы один общий друг: это пользователь 4 (общие друзья 2 и 3) и пользователь 5 (общий друг 2). С пользователем 4 общих друзей больше, поэтому в список возможных друзей попадает только он.

У пользователя 5 есть два пользователя, которые не являются его друзьями и с которыми у него есть хотя бы один общий друг: это пользователь 1 (общий друг 2) и пользователь 4 (общий друг 2). Количество общих друзей одинаковое, поэтому оба этих пользователя попадают в список возможных друзей.

У пользователя 7 вообще нет друзей, поэтому ни один пользователь не удовлетворяет требованиям списка возможных друзей.
*/

public class PossibleFriends {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        var friends = Stream.iterate(1, i -> scanner.hasNextLine(), i -> i + 1)
                .map(i -> scanner.nextLine())
                .map(inputLine -> {
                    String[] parts = inputLine.split("\\s+");
                    return Arrays.asList(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
                })
                .collect(Collectors.toList());

        findFriends(friends).forEach(y -> System.out.println(y.toString()
                .replace(",", "")
                .replace("[", "")
                .replace("]", "")));
    }

    private static List<Collection<Integer>> findFriends(List<List<Integer>> friends) {
        Map<Integer, List<Integer>> friendsMap = new HashMap<>();

        for (List<Integer> s : friends.stream().skip(1).collect(Collectors.toList())) {
            friendsMap.computeIfAbsent(s.get(0), k -> new ArrayList<>()).add(s.get(1));
            friendsMap.computeIfAbsent(s.get(1), k -> new ArrayList<>()).add(s.get(0));
        }

        return IntStream.rangeClosed(1, friends.get(0).get(0))
                .parallel()
                .mapToObj(n -> {
                    if (null == friendsMap.get(n)) return List.of(0);

                    var commonFriends = friendsMap.get(n).stream()
                            .map(friendsMap::get)
                            .flatMap(Collection::stream)
                            .filter(i -> !i.equals(n))
                            .filter(i -> !friendsMap.get(n).contains(i))
                            .distinct()
                            .collect(Collectors.toList());

                    Map<Integer, Integer> filteredFriendsMap = new HashMap<>();
                    for (Integer friend : commonFriends) {
                        List<Integer> intersect = new ArrayList<>(friendsMap.get(friend));
                        intersect.retainAll(friendsMap.get(n));
                        filteredFriendsMap.put(friend, intersect.size());
                    }

                    int maxValue;
                    if (filteredFriendsMap.isEmpty()) {
                        maxValue = 0;
                    } else {
                        maxValue = filteredFriendsMap.entrySet().stream()
                                .max(Map.Entry.comparingByValue())
                                .get()
                                .getValue();
                    }

                    Map<Integer, Integer> maxValuesMap;
                    if (0 == maxValue) {
                        maxValuesMap = Map.of(0, 0);
                    } else {
                        maxValuesMap = filteredFriendsMap.entrySet().stream()
                                .filter(x -> x.getValue().compareTo(maxValue) == 0)
                                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
                    }
                    return new TreeSet<>(maxValuesMap.keySet());
                })
                .collect(Collectors.toList());
    }
}
