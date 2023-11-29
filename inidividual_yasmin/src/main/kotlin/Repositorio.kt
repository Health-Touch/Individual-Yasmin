import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.JdbcTemplate

class Repositorio {

    private lateinit var jdbcTemplate:JdbcTemplate

    fun criarTabelas() {
        jdbcTemplate.execute("""
        IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'Plano')
        BEGIN
            CREATE TABLE Plano (
                idPlano INT PRIMARY KEY IDENTITY(1,1),
                tipoPlano VARCHAR(45),
                descricao VARCHAR(45)
            );
        END;
    """)

        jdbcTemplate.execute("""
        IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'Empresa')
        BEGIN
            CREATE TABLE Empresa (
                idEmpresa INT IDENTITY(1,1),
                NomeFantasia VARCHAR(45),
                CNPJ CHAR(14),
                inicioContrato DATE,
                telFixo CHAR(10),
                fkPlano INT, 
                CONSTRAINT fk_plano_empresa FOREIGN KEY(fkPlano) REFERENCES Plano(idPlano),
                CONSTRAINT pkComposta PRIMARY KEY (idEmpresa, fkPlano)
            );
        END;
    """)

        // Repita o padrão acima para as outras tabelas
        // ...

        jdbcTemplate.execute("""
        IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'parametro')
        BEGIN
            CREATE TABLE parametro (
                idParametro INT IDENTITY(1,1),
                critico FLOAT,
                alerta FLOAT, 
                fkComponente INT,
                CONSTRAINT fk_componete_parametro FOREIGN KEY(fkComponente) REFERENCES Componente(idComponente),
                CONSTRAINT pkCompostaParametro PRIMARY KEY (idParametro, fkComponente)
            );
        END;
    """)
    }


    fun iniciar(){
        jdbcTemplate = Conexao.jdbcTemplate!!
    }


    fun verificarLogin(email:String, senha:String):Int?{
        return jdbcTemplate.queryForObject("""
            select count(idColaborador) from Colaborador where email = '$email' and senha = '$senha'
        """, Int::class.java)
    }

    fun dadosColaborador(email: String, senha: String):Colaborador?{
        return jdbcTemplate.queryForObject("""
            SELECT c.nome, c.fkEmpresa, c.fkNivelAcesso, e.fkPlano FROM Colaborador AS c JOIN Empresa AS e ON c.fkEmpresa = e.idEmpresa WHERE email = '$email' AND senha = '$senha'
        """,BeanPropertyRowMapper(Colaborador::class.java))
    }

    fun exibirMaquinas(fkEmpresa:Int?):List<Maquina>{
        return jdbcTemplate.query("""
            SELECT * FROM Maquina WHERE fkEmpresa = $fkEmpresa
        """, BeanPropertyRowMapper(Maquina::class.java))
    }

    fun validarMaquina(idMaquina:Int, fkEmpresa: Int?):Int?{
        return jdbcTemplate.queryForObject("""
            SELECT COUNT(*) FROM Maquina WHERE idMaquina = $idMaquina AND fkEmpresa = $fkEmpresa
        """, Int::class.java)
    }

    fun pegarDadosMaquina(idMaquina: Int):Maquina?{
        return jdbcTemplate.queryForObject("""
            SELECT * FROM Maquina where idMaquina = $idMaquina
        """, BeanPropertyRowMapper(Maquina::class.java))
    }

    //talvez não precise mais disso por causa da função acima :)
//    fun recuperarTipoMaquina(idMaquina:Int, fkEmpresa: Int?):Int?{
//        return jdbcTemplate.queryForObject("""
//            SELECT fkTipoMaquina FROM Maquina WHERE idMaquina = $idMaquina AND fkEmpresa = $fkEmpresa
//        """, Int::class.java)
//    }

    fun cadastrarMonitoramentoCpu(monitoramento: Monitoramento, maquina: Maquina){
        jdbcTemplate.execute("""
            insert into MonitoramentoYasmin (porcentagem, dataHora, frequencia, fkComponente, fkMaquina, fkPlanoEmpresa, fkTipoMaquina, fkEmpresaMaquina) values
            ('${monitoramento.porcentagemCpu}', '${monitoramento.dataHora}', '${monitoramento.frequencia}', 1, ${maquina.idMaquina}, ${maquina.fkPlanoEmpresa}, ${maquina.fkTipoMaquina}, ${maquina.fkEmpresa})
        """.trimIndent())
    }

    fun cadastrarMonitoramentoRam(monitoramento: Monitoramento, maquina: Maquina){
        jdbcTemplate.execute("""
            insert into MonitoramentoYasmin (porcentagem, dataHora, ramDisponivel, ramUsada, fkComponente, fkMaquina, fkPlanoEmpresa, fkTipoMaquina, fkEmpresaMaquina) values
            ('${monitoramento.porcentagemRam}', '${monitoramento.dataHora}', '${monitoramento.ramDisponivel}', '${monitoramento.ramUsada}', 2, ${maquina.idMaquina}, ${maquina.fkPlanoEmpresa}, ${maquina.fkTipoMaquina}, ${maquina.fkEmpresa})
        """.trimIndent())
    }

    fun cadastrarProcesso(processo: Processo, maquina: Maquina){
        jdbcTemplate.execute("""
           insert into Processo (nome, PID, uso_cpu, uso_ram, fkMaquina, fkEmpresa, fkTipoMaquina, fkStatusMaquina) values
            ('${processo.nome}', '${processo.pid}', '${processo.usoCpu}', '${processo.usoRam}', ${maquina.idMaquina}, ${maquina.fkEmpresa}, ${maquina.fkTipoMaquina}, ${maquina.fkStatusMaquina})
        """)
    }

    //

//    fun cadastrarCpu(cCpu:Monitoramento){
//        jdbcTemplate.execute("""
//           insert into monitoramento (porcentagem, dataHora, fkComponente, fkMaquina, fkPlanoEmpresa, fkTipoMaquina, fkEmpresaMaquina) values
//            ('${cCpu.porcentagem}', '${cCpu.dataHora}', 1, 1, 1, 1, 1)
//        """)
//    }
//
//    fun cadastrarRam(cRam:Monitoramento){
//        jdbcTemplate.execute("""
//           insert into monitoramento (porcentagem, dataHora, fkComponente, fkMaquina, fkPlanoEmpresa, fkTipoMaquina, fkEmpresaMaquina) values
//            ('${cRam.porcentagem}', '${cRam.dataHora}', 2, 1, 1, 1, 1)
//        """)
//    }



}