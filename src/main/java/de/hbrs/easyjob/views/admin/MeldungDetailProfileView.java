package de.hbrs.easyjob.views.admin;

import de.hbrs.easyjob.controllers.SessionController;

public class MeldungDetailProfileView extends MeldungDetailView{

    private final SessionController sessionController;
    String profil;

    public MeldungDetailProfileView(SessionController sessionController){
        super(sessionController);

        this.sessionController = sessionController;
    }
}
