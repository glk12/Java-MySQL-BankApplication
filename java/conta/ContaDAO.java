package conta;


import cliente.Cliente;
import cliente.DadosCadastroCliente;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ContaDAO {
    
    private Connection conn;
    
    public ContaDAO(Connection conn){
        this.conn=conn;
    }
    public void salvar(DadosAberturaConta dadosConta){
        
        
            var cliente= new Cliente(dadosConta.dadosCliente());
            var conta=new Conta(dadosConta.numero(),BigDecimal.ZERO,cliente,true);
            
            String sql="INSERT INTO conta(numero,saldo,cliente_nome,cliente_cpf,cliente_email,esta_ativa) VALUES(?,?,?,?,?,?)";
            
            try {
                PreparedStatement preparedstatement=conn.prepareStatement(sql);

                preparedstatement.setInt(1,conta.getNumero());
                preparedstatement.setBigDecimal(2,BigDecimal.ZERO);
                preparedstatement.setString(3,cliente.getNome());
                preparedstatement.setString(4,cliente.getCpf());
                preparedstatement.setString(5,cliente.getEmail());
                preparedstatement.setBoolean(6, true);
                
                preparedstatement.execute();
                preparedstatement.close();
                conn.close();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }       
    }
    
    public Set<Conta> listar(){
        PreparedStatement ps;
        ResultSet rs;
        Set<Conta> contas = new HashSet<>();
        
        String sql= "SELECT * FROM conta WHERE esta_ativa=true";
        
        try {
            ps=conn.prepareStatement(sql);
            rs=ps.executeQuery();
            
            while(rs.next()){
                Integer numero= rs.getInt(1);
                BigDecimal saldo=rs.getBigDecimal(2);
                String nome=rs.getString(3);
                String cpf=rs.getString(4);
                String email=rs.getString(5);
                Boolean estaativa=rs.getBoolean(6);
                
                DadosCadastroCliente dadosCadastroCliente= new DadosCadastroCliente(nome,cpf,email);
                Cliente cliente= new Cliente(dadosCadastroCliente);
                
                contas.add(new Conta(numero,saldo,cliente,estaativa));               
            }
            rs.close();
            ps.close();
            conn.close();       
            
        } catch (SQLException ex) {
            Logger.getLogger(ContaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return contas;

        
    }
    public Conta listarPorNumero(Integer numero) {
        String sql = "SELECT * FROM conta WHERE numero = " + numero + " and esta_ativa = true";

        PreparedStatement ps;
        ResultSet resultSet;
        Conta conta = null;
        try {
            ps = conn.prepareStatement(sql);
            resultSet = ps.executeQuery();

            while (resultSet.next()) {
                Integer numeroRecuperado = resultSet.getInt(1);
                BigDecimal saldo = resultSet.getBigDecimal(2);
                String nome = resultSet.getString(3);
                String cpf = resultSet.getString(4);
                String email = resultSet.getString(5);
                Boolean estaAtiva = resultSet.getBoolean(6);

                DadosCadastroCliente dadosCadastroCliente =
                        new DadosCadastroCliente(nome, cpf, email);
                Cliente cliente = new Cliente(dadosCadastroCliente);

                conta = new Conta(numeroRecuperado, saldo, cliente, estaAtiva);
            }
            resultSet.close();
            ps.close();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return conta;
    }
    
    public void alterar(Integer numero, BigDecimal valor){
        PreparedStatement ps;
        String sql="UPDATE conta SET saldo = ? WHERE numero= ?";
        
        try {
            conn.setAutoCommit(false);
            
            ps=conn.prepareStatement(sql);
            ps.setBigDecimal(1, valor);
            ps.setInt(2,numero);
            
            ps.execute();
            ps.close();
            conn.close();
            conn.commit();
            
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }
            
    }
    public void deletar(Integer numero){
        PreparedStatement ps;
        String sql= "DELETE FROM conta WHERE numero = ?";
        
        try {
            ps=conn.prepareStatement(sql);
            ps.setInt(1,numero);
            
            ps.execute();
            ps.close();
            conn.close();
          
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
    public void desativarConta(Integer numero){
        PreparedStatement ps;
        String sql="UPDATE conta SET esta_ativo = false WHERE numero = ?";
        
        try {
            ps=conn.prepareStatement(sql);
            ps.setInt(1,numero);
            ps.execute();
            ps.close();
            conn.close();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}
