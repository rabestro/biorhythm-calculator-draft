package lv.id.jc.biorhythm.stage;

import lv.id.jc.biorhythm.model.Context;
import lv.id.jc.biorhythm.report.AgeInfo;
import lv.id.jc.biorhythm.report.DailyReport;
import lv.id.jc.biorhythm.report.ZodiacInfo;
import lv.id.jc.biorhythm.service.AskBirthday;
import lv.id.jc.biorhythm.service.Message;

public class Stage2 {
    public static void main(String[] args) {
        // Stage 1
        new Message("welcome").run();

        var context = new Context();

        new AskBirthday(context).run();
        new AgeInfo(context).run();
        new ZodiacInfo(context).run();

        // Stage 2
        new DailyReport(context).run();
    }
}
