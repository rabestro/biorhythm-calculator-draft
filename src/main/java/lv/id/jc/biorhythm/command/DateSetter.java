package lv.id.jc.biorhythm.command;

import lv.id.jc.biorhythm.model.Context;

import java.time.LocalDate;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;
import static java.util.regex.Pattern.compile;

public class DateSetter extends DateCommand {
    private final Map<Pattern, Function<Matcher, LocalDate>> map = Map.of(
            compile("(?<date>\\d{4}-\\d{2}-\\d{2})"),
            m -> LocalDate.parse(m.group("date")),
            compile("(?<year>\\d{4})-(?<month>\\d{2})"),
            m -> context.getDate().withYear(parseInt(m.group("year"))).withMonth(parseInt(m.group("month"))),
            compile("(?<year>\\d{4})"), m -> context.getDate().withYear(parseInt(m.group("year")))
    );

    public DateSetter(Context context) {
        super(context);
    }

    @Override
    public boolean test(String request) {
        for (var entry : map.entrySet()) {
            final var matcher = entry.getKey().matcher(request);
            if (matcher.matches()) {
                final var date = entry.getValue().apply(matcher);
                context.setDate(date);
                return true;
            }
        }
        return false;
    }
}
