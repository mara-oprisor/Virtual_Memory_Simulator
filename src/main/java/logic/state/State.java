package logic.state;

import view.UIController;

public interface State {
    public void execute(UIController context);
}
