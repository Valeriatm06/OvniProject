package co.edu.uptc.interfaces;

public interface OvniInterface {
    
    public interface Model {
        public void setPresenter(Presenter presenter);
    }

    public interface Presenter {
        public void setModel(Model model);

        public void setView(View view);
    }

    public interface View {
        public void setPresenter(Presenter presenter);

        public void begin();
    }
}
