package lv.id.jc.biorhythm.command;

import lv.id.jc.biorhythm.model.Context;
import lv.id.jc.biorhythm.ui.Component;

public abstract class AbstractCommand extends Component implements Command {
    public AbstractCommand(Context context) {
        super(context);
    }

    @Override
    public String help() {
        if (resourceBundle.containsKey("help")) {
            return resourceBundle.getString("help");
        }

        final var className = this.getClass().getSimpleName();
        return String.format("%-15s - run component \"%s\"%n", getCommand(), className);
    }

    @Override
    public Boolean apply(String request) {
        if (getCommand().equalsIgnoreCase(request)) {
            this.run();
            return true;
        }
        return false;
    }
}
