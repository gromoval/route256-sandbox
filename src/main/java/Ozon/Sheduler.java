package Ozon;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

/*
H. Планировщик задач (30 баллов)
ограничение по времени на тест3 секунды
ограничение по памяти на тест256 мегабайт
вводстандартный ввод
выводстандартный вывод
Представьте, вы собрали собственный сервер из n разнородных процессоров и теперь решили создать для него простейший планировщик задач.

Ваш сервер состоит из n процессоров. Но так как процессоры разные, то и достигают они одинаковой скорости работы при разном энергопотреблении. А именно, i-й процессор в нагрузке тратит ai энергии за одну секунду.

Вашему серверу в качестве тестовой нагрузки придет m задач. Про каждую задачу вам известны два значения: tj и lj — момент времени, когда задача j придет и время выполнения задачи в секундах.

Для начала вы решили реализовать простейший планировщик, ведущий себя следующим образом: в момент tj прихода задачи, вы выбираете свободный процессор с минимальным энергопотреблением и выполняете данную задачу на выбранном процессоре все заданное время. Если к моменту прихода задачи свободных процессоров нет, то вы просто отбрасываете задачу.

Процессор, на котором запущена задача j будет занят ровно lj секунд, то есть освободится ровно в момент tj+lj и в этот же момент уже может быть назначен для выполнения какой-то другой задачи.

Определите суммарное энергопотребление вашего сервера при обработке m заданных задач (будем считать, что процессоры в простое не потребляют энергию).

Неполные решения этой задачи (например, недостаточно эффективные) могут быть оценены частичным баллом.

Входные данные
В первой строке заданы два целых числа n и m (1≤n,m≤3⋅105) — количество процессоров и задач соответственно.

Во второй строке заданы n целых чисел a1,a2,…,an (1≤ai≤106) — энергопотребление соответствующих процессоров под нагрузкой в секунду. Все энергопотребления различны.

В следующих m строках заданы описания задач: по одному в строке. В j-й строке заданы два целых числа tj и lj (1≤tj≤109; 1≤lj≤106) — момент прихода j-й задачи и время ее выполнения.

Все времена прихода tj различны, и задачи заданы в порядке времени прихода.

Выходные данные
Выведите единственное число — суммарное энергопотребление сервера, если потреблением энергии в простое можно пренебречь.

Пример
входные данные
4 7
3 2 6 4
1 3
2 5
3 7
4 10
5 5
6 100
9 2

выходные данные
105

Примечание
Рассмотрим работу планировщика по секундно:

t=1: приходит первая задача, все процессоры свободны. Задача занимает второй процессор на 3 секунды.
t=2: приходит вторая задача. Второй процессор занят, а потому задача занимает первый процессор на 5 секунд.
t=3: приходит третья задача и занимает четвертый процессор на 7 секунд.
t=4: приходит четвертая задача. Второй процессор освободился в данный момент, а потому его и занимает задача на 10 секунд.
t=5: приходит пятая задача и занимает последний свободный на данный момент процессор (третий) на 5 секунд.
t=6: приходит шестая задача. Все процессоры еще заняты, а потому задача отбрасывается.
t=7: освобождается первый процессор.
t=9: приходит седьмая задача и занимает первый процессор на 2 секунды.
t=10: освобождаются третий и четвертый процессоры.
t=11: освобождается первый процессор.
t=14: освобождается второй процессор.
Общее энергопотребление равно 3⋅2 + 5⋅3 + 7⋅4 + 10⋅2 + 5⋅6 + 2⋅3 = 6+15+28+20+30+6 = 105.
*/

public class Sheduler {
    public static void main(String[] args) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        var input = reader.lines()
                .map(inputLine -> {
                    String[] parts = inputLine.split("\\s+");
                    return Arrays.stream(parts).map(Integer::parseInt).collect(Collectors.toList());
                })
                .collect(Collectors.toList());

        System.out.println(calcConsumption(input));
    }

    private static long calcConsumption(List<List<Integer>> input) {
        long result = 0L;

        var filteredInput = input.stream().skip(2).collect(Collectors.toList());
        TreeSet<Integer> procFreeSet = new TreeSet<>(input.get(1));
        TreeSet<Proc> procBusySet = new TreeSet<>();

        for (int i = 0; i < input.get(0).get(1); i++) {
            var taskStartTime = filteredInput.get(i).get(0);
            var tastDuration = filteredInput.get(i).get(1);

            if (procBusySet.size() > 0) {
                var setCount = procBusySet.size();

                for (int j = 0; j < setCount; j++) {
                    var procBusy = procBusySet.first();

                    if (procBusy.getEndWorkTime() <= taskStartTime) {
                        procBusySet.remove(procBusy);
                        procBusy.setEndWorkTime(0);

                        procFreeSet.add(procBusy.getPower());
                    } else {
                        break;
                    }
                }
            }

            if (procFreeSet.size() > 0) {
                var procPower = procFreeSet.first();
                procFreeSet.remove(procPower);

                result += (long) filteredInput.get(i).get(1) * procPower;
                procBusySet.add(new Proc(procPower, taskStartTime + tastDuration));
            }
        }
        return result;
    }
}

class Proc implements Comparable<Proc> {
    private Integer power;
    private Integer endWorkTime;

    public Proc(int power, int endWorkTime) {
        this.power = power;
        this.endWorkTime = endWorkTime;
    }

    public int getPower() {
        return power;
    }

    public int getEndWorkTime() {
        return endWorkTime;
    }

    public void setEndWorkTime(int endWorkTime) {
        this.endWorkTime = endWorkTime;
    }

    @Override
    public int compareTo(Proc o) {
        var result = this.endWorkTime.compareTo(o.endWorkTime);
        if (0 == result)
            result = this.power.compareTo(o.power);
        return result;
    }
}
