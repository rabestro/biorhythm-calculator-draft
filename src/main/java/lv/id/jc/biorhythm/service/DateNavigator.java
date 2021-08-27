package lv.id.jc.biorhythm.service;

import lombok.val;
import lv.id.jc.biorhythm.model.Context;
import lv.id.jc.biorhythm.report.*;
import lv.id.jc.biorhythm.ui.Component;

import java.time.LocalDate;
import java.time.Period;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.regex.Pattern;

import static java.lang.System.Logger.Level.TRACE;

/**
 * @deprecated since Broker class introduced.
 */
@Deprecated(forRemoval = true)
public class DateNavigator extends Component implements Runnable {
    private static final Pattern PLUS_MINUS = Pattern.compile("([-+])(\\d+)([dwmyq])");
    private static final Map<String, BiFunction<LocalDate, Long, LocalDate>> moveOperators = Map.of(
            "+d", LocalDate::plusDays, "-d", LocalDate::minusDays,
            "+w", LocalDate::plusWeeks, "-w", LocalDate::minusWeeks,
            "+m", LocalDate::plusMonths, "-m", LocalDate::minusMonths,
            "+y", LocalDate::plusYears, "-y", LocalDate::minusYears,
            "+q", (d, n) -> d.plus(Period.of(0, n.intValue() * 3, 0)),
            "-q", (d, n) -> d.minus(Period.of(0, n.intValue() * 3, 0))
    );
    private static final Map<String, Function<Context, Runnable>> reports = Map.of(
            "print daily", DailyReport::new, "print annual", AnnualReport::new,
            "print weekly", WeeklyReport::new, "print age", AgeInfo::new,
            "graph triple", TripleChart::new, "print summary", SummaryReport::new
    );
    private static final Set<String> information = Set.of("help", "info");
    private static final Set<String> exit = Set.of("exit", "quit");

    private final Map<String, LocalDate> setOperators;

    public DateNavigator(final Context context) {
        super(context);
        setOperators = Map.of("today", LocalDate.now(), "now", LocalDate.now(),
                "epoch", LocalDate.EPOCH, "birthday", context.getBirthday(),
                "tomorrow", LocalDate.now().plusDays(1L), "after tomorrow", LocalDate.now().plusDays(2L),
                "yesterday", LocalDate.now().minusDays(1L), "before yesterday", LocalDate.now().minusDays(2L));
    }

    @Override
    public void run() {
        printf("welcome", context.getBirthday(), context.getDate());
        while (true) {
            printf("prompt", context.getDate());
            val command = scanner.nextLine().toLowerCase();
            if (exit.contains(command)) {
                return;
            }

            if (information.contains(command)) {
                printf(command);
                continue;
            }
            if (reports.containsKey(command)) {
                reports.get(command).apply(context).run();
                continue;
            }
            if (setOperators.containsKey(command)) {
                context.setDate(setOperators.get(command));
                continue;
            }
            val plusMinus = PLUS_MINUS.matcher(command);
            if (plusMinus.matches()) {
                val sign = plusMinus.group(1);
                val number = Long.parseLong(plusMinus.group(2));
                val unit = plusMinus.group(3);
                LOGGER.log(TRACE, "sign = {0}, number = {1}, unit = {2}", sign, number, unit);
                context.setDate(moveOperators.get(sign + unit).apply(context.getDate(), number));
            }
            printf("unrecognized", command);
        }
    }

}
