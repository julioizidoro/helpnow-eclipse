package br.com.travelmate.ManagerBean;

import br.com.travelmate.facade.InteracaoFacade;
import br.com.travelmate.model.Chamado;
import br.com.travelmate.model.Interacao;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

@Named
@ViewScoped
public class InteracaoMB implements Serializable{
    
    private Interacao interacao;
    private List<Interacao> listaInteracao;
    private Chamado chamado;
    
    @PostConstruct
    public void init(){
        FacesContext fc = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) fc.getExternalContext().getSession(false);
        chamado = (Chamado) session.getAttribute("chamado");
        session.removeAttribute("chamado");
        interacao = new Interacao();
        if (interacao!=null){
            gerarListaInteracao();
        }else{
            listaInteracao = new ArrayList<Interacao>();
        }
    }

    public Interacao getInteracao() {
        return interacao;
    }

    public void setInteracao(Interacao interacao) {
        this.interacao = interacao;
    }

    public List<Interacao> getListaInteracao() {
        return listaInteracao;
    }

    public void setListaInteracao(List<Interacao> listaInteracao) {
        this.listaInteracao = listaInteracao;
    }

    public Chamado getChamado() {
        return chamado;
    }

    public void setChamado(Chamado chamado) {
        this.chamado = chamado;
    }
    
    
    
    public void gerarListaInteracao() {
        String sql = "select i from Interacao i where i.chamado.idchamado=" + chamado.getIdchamado();
        InteracaoFacade interacaoFacade = new InteracaoFacade();
        listaInteracao = interacaoFacade.listar(sql);
        if (listaInteracao == null) {
            listaInteracao = new ArrayList<Interacao>();
        }
    }
    
}
