package victor_vasconcelos.icomp.ufam.edu.br.composicaocolaborativa;

import java.io.Serializable;

/**
 * Created by Usuario on 28/04/2016.
 */
public class Usuarios implements Serializable {
    private static final long serialVersionUID = 1L;
    private int idUsuario;
    private String login, nome, senha, sons;

    public Usuarios(int id, String login, String nome, String senha, String sons ){
        this.idUsuario = id;
        this.login = login;
        this.nome = nome;
        this.senha = senha;
        this.sons = sons;
    }

    public Usuarios(String login, String nome, String senha) {
        this.login = login;
        this.nome = nome;
        this.senha = senha;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }


    public String getSons() {
        return sons;
    }

    public void setSons(String sons) {
        this.sons = sons;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }
}
