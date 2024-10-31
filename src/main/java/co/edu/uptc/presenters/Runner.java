package co.edu.uptc.presenters;

import co.edu.uptc.interfaces.OvniInterface;
import co.edu.uptc.models.OvniModel;
import co.edu.uptc.views.OvniMainFrame.OvniMainView;

public class Runner {
    
    private OvniInterface.Model model;
    private OvniInterface.Presenter presenter;
    private OvniInterface.View view;
    
    private void makeMVP(){
        this.model = new OvniModel();
        this.presenter = new MainPresenter();
        this.view = new OvniMainView();

        view.setPresenter(presenter);
        model.setPresenter(presenter);
        presenter.setModel(model);
        presenter.setView(view);
    }

    public void run(){
        makeMVP();
        view.begin();
    }
}
