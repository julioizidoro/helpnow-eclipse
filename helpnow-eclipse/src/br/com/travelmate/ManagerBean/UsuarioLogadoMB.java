/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.travelmate.ManagerBean;

import br.com.travelmate.facade.UsuarioFacade;
import br.com.travelmate.model.Usuario;
import br.com.travelmate.util.Criptografia;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 *
 * @author Wolverine
 */
@Named
@SessionScoped
public class UsuarioLogadoMB implements Serializable{
    
    private static final long serialVersionUID = 1L;
    private Usuario usuario;
    private String novaSenha;
    private String confirmaNovaSenha;

    @PostConstruct
    public void ini(){
        usuario = new Usuario();
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }


    public String getNovaSenha() {
        return novaSenha;
    }

    public void setNovaSenha(String novaSenha) {
        this.novaSenha = novaSenha;
    }

    public String getConfirmaNovaSenha() {
        return confirmaNovaSenha;
    }

    public void setConfirmaNovaSenha(String confirmaNovaSenha) {
        this.confirmaNovaSenha = confirmaNovaSenha;
    }

    public String validarUsuario(){
        if ((usuario.getLogin()==null) && (usuario.getSenha()==null)){
            FacesContext context = FacesContext.getCurrentInstance();
             context.addMessage(null, new FacesMessage("Acesso Negado", "Erro!"));
        }else{
            String senha = "";
            try {
                senha = Criptografia.encript(usuario.getSenha());
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(UsuarioLogadoMB.class.getName()).log(Level.SEVERE, null, ex);
            }
            usuario.setSenha(senha);
            UsuarioFacade usuarioFacade = new UsuarioFacade();
            usuario = usuarioFacade.consultar(usuario.getLogin(), usuario.getSenha());
            if (usuario==null){
              FacesContext context = FacesContext.getCurrentInstance();
              context.addMessage(null, new FacesMessage("Login Invalido", "Erro!"));
            }else {
                if(usuario.getDepartamento().equalsIgnoreCase("TI")){
                     return "consultaChamadoSuporte";
                }else{
                    return "consultaChamado";
                }
            }
        }
        usuario = new Usuario();
        return "";
    }
    
    
     public void erroLogin(String mensagem) {
        FacesContext context = FacesContext.getCurrentInstance();
             context.addMessage(null, new FacesMessage("Acesso Negado", "Erro!"));
    }
     
    public void validarTrocarSenha() {
        if ((usuario.getLogin() != null) && (usuario.getSenha() == null)) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Login Invalido", "Erro!"));
        } else {
            UsuarioFacade usuarioFacade = new UsuarioFacade();
             String senha = "";
            try {
                senha = Criptografia.encript(usuario.getSenha());
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(UsuarioLogadoMB.class.getName()).log(Level.SEVERE, null, ex);
            }
            usuario.setSenha(senha);
            usuario = usuarioFacade.consultar(usuario.getLogin(), usuario.getSenha());
            if (usuario == null) {
                FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage(null, new FacesMessage("Acesso Negado", "Erro!"));
            }
        }
    }
    
    public String confirmaNovaSenha() {
        if ((novaSenha.length() > 0) && (confirmaNovaSenha.length() > 0)) {
            if (novaSenha.equalsIgnoreCase(confirmaNovaSenha)) {
                UsuarioFacade usuarioFacade = new UsuarioFacade();
                usuario.setSenha(novaSenha);
                usuario = usuarioFacade.salvar(usuario);
                novaSenha = "";
                confirmaNovaSenha = "";
                return "inicial";
            } else {
                novaSenha = "";
                confirmaNovaSenha = "";
                FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage(null, new FacesMessage("Acesso Negado", "Erro!"));
            }

        } else {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Acesso Negado", "Erro!"));
        }
        return "";
    }
    
    public String cancelarTrocaSenha(){
        usuario = new Usuario();
        novaSenha="";
        confirmaNovaSenha="";
        return "index";
    }
    public String deslogar(){
        usuario.setIdusuario(null);
        Map sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();  
        sessionMap.clear();  
        return "login";
    }
}
