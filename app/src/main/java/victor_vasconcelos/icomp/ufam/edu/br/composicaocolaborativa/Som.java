package victor_vasconcelos.icomp.ufam.edu.br.composicaocolaborativa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Usuario on 27/05/2016.
 */
public class Som {
    private String nome;
    private int sourceRaw;

    public Som(String nome, int sourceRaw){
        this.nome = nome;
        this.sourceRaw = sourceRaw;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setSourceRaw(int source) {
        this.sourceRaw = source;
    }

    @Override
    public String toString(){
        return nome;
    }

    public int getSourceRaw() {
        return sourceRaw;
    }

    static public List<Som> populateSomList(){
        List<Som> somList = new ArrayList<>();
        somList.add(new Som("Acua",R.raw.acaua));
        somList.add(new Som("Arapacu de Bico-Branco",R.raw.arapacu_de_bico_branco));
        somList.add(new Som("Arapacu de Garganta-Amarela",R.raw.arapacu_de_garganta_amarela));
        somList.add(new Som("Arapacu Ferrugem",R.raw.arapacu_ferrugem));
        somList.add(new Som("Beneditinho de Testa Vermelha",R.raw.beneditinho_de_testa_vermelha));
        somList.add(new Som("Bentevizinho do Brejo",R.raw.bentevizinho_do_brejo));
        somList.add(new Som("Carão",R.raw.carao));
        somList.add(new Som("Casaca de Couro-de-Lama",R.raw.casaca_de_couro_de_lama));
        somList.add(new Som("Coro-Coro",R.raw.coro_coro));
        somList.add(new Som("Garinchão de Barriga-Vermelha",R.raw.garinchao_de_barriga_vermelha));
        somList.add(new Som("Gaturamo de Barriga-Vermelha",R.raw.gaturamo_de_barriga_branca));
        somList.add(new Som("Iratua Pequeno",R.raw.irataua_pequeno));
        somList.add(new Som("Pica Pau Bufador",R.raw.pica_pau_bufador));
        somList.add(new Som("Pica Pau de Peito Pontilhado",R.raw.pica_pau_de_peito_pontilhado));
        somList.add(new Som("Picaparra",R.raw.picaparra));
        somList.add(new Som("Sabiá Coleira",R.raw.sabia_coleira));
        somList.add(new Som("Socó Boi",R.raw.soco_boi));
        somList.add(new Som("Tinguaçu Ferrugem",R.raw.tinguacu_ferrugem));
        somList.add(new Som("Tucano de Bico Pretook",R.raw.tucano_de_bico_pretook));
        return somList;
    }

    static public HashMap<String, Som> populateKeyAudios(){
        HashMap<String, Som> audioList = new HashMap<>();
        List<Som> somAuxList = Som.populateSomList();
        audioList.put("Acua", somAuxList.get(0));
        audioList.put("Arapacu de Bico-Branco", somAuxList.get(1));
        audioList.put("Arapacu de Garganta-Amarela", somAuxList.get(2));
        audioList.put("Arapacu Ferrugem", somAuxList.get(3));
        audioList.put("Beneditinho de Testa Vermelha", somAuxList.get(4));
        audioList.put("Bentevizinho do Brejo", somAuxList.get(5));
        audioList.put("Carão",somAuxList.get(6));
        audioList.put("Casaca de Couro-de-Lama", somAuxList.get(7));
        audioList.put("Coro-Coro", somAuxList.get(8));
        audioList.put("Garinchão de Barriga-Vermelha", somAuxList.get(9));
        audioList.put("Gaturamo de Barriga-Vermelha", somAuxList.get(10));
        audioList.put("Iratua Pequeno", somAuxList.get(11));
        audioList.put("Pica Pau Bufador", somAuxList.get(12));
        audioList.put("Pica Pau de Peito Pontilhado", somAuxList.get(13));
        audioList.put("Picaparra", somAuxList.get(14));
        audioList.put("Sabiá Coleira", somAuxList.get(15));
        audioList.put("Socó Boi", somAuxList.get(16));
        audioList.put("Tinguaçu Ferrugem", somAuxList.get(17));
        audioList.put("Tucano de Bico Pretook", somAuxList.get(18));

        return audioList;
    }
}
