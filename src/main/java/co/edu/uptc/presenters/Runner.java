package co.edu.uptc.presenters;

import co.edu.uptc.interfaces.UfoInterface;
import co.edu.uptc.models.UfoModel;
import co.edu.uptc.views.UfoMainFrame.UfoMainView;

public class Runner {
    
    private UfoInterface.Model model;
    private UfoInterface.Presenter presenter;
    private UfoInterface.View view;
    
    private void makeMVP(){
        this.model = new UfoModel();
        this.presenter = new MainPresenter();
        this.view = new UfoMainView();

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
