package co.edu.uptc.presenters;

import co.edu.uptc.interfaces.OvniInterface;

public class MainPresenter implements OvniInterface.Presenter{

    private OvniInterface.Model model;
    private OvniInterface.View view;

    @Override
    public void setModel(OvniInterface.Model model) {
        this.model = model;
    }

    @Override
    public void setView(OvniInterface.View view) {
       this.view = view;
    }

}
