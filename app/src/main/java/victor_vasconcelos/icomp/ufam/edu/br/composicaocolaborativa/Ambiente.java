package victor_vasconcelos.icomp.ufam.edu.br.composicaocolaborativa;

/**
 * Created by Victor Freitas Vasconcelos on 01/06/2016.
 */
public class Ambiente {

    private int id;
    private String descricao;
    private double latitude, longitude;
    private float raio;
    private int totalPessoas;

    public Ambiente(int id, String descricao,double longitude, double latitude, float raio, int totalPessoas) {
        this.id = id;
        this.descricao = descricao;
        this.latitude = latitude;
        this.longitude = longitude;
        this.raio = raio;
        this.totalPessoas = totalPessoas;
    }

    public int getTotalPessoas() {
        return totalPessoas;
    }

    public void setTotalPessoas(int totalPessoas) {
        this.totalPessoas = totalPessoas;
    }

    public float getRaio() {
        return raio;
    }

    public void setRaio(float raio) {
        this.raio = raio;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getid() {
        return id;
    }
}
