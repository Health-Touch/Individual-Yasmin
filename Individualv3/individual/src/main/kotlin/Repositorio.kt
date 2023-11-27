import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.JdbcTemplate

class Repositorio {

    private lateinit var jdbcTemplate:JdbcTemplate

    fun criarTabelas(){
        jdbcTemplate.execute("""
            create table if not exists plano(
            idPlano Int primary key auto_increment,
            tipoPlano varchar(45),
            descricao varchar(45)
            );
        """)

        jdbcTemplate.execute("""
            create table if not exists empresa(
            idEmpresa int auto_increment,
            NomeFantasia varchar(45),
            CNPJ char(14),
            inicioContrato date,
            telFixo char(10),
            fkPlano int, 
            constraint fk_plano_empresa foreign key(fkPlano) references Plano(idPlano),
            constraint pkComposta primary key (idEmpresa, fkPlano)
            );
        """)

        jdbcTemplate.execute("""
            create table if not exists endereco(
            idEndereco int auto_increment,
            rua varchar(45),
            num int,
            estado varchar(45),
            CEP char(8),
            cidade varchar(45),
            fkEmpresa int, 
            constraint fk_empresa_endereco foreign key(fkEmpresa) references Empresa(idEmpresa),
            constraint pk_composta_endereco primary key (idEndereco, fkEmpresa) 
            );
        """)


        jdbcTemplate.execute("""
            create table if not exists nivelAcesso(
            idNivelAcesso int primary key auto_increment,
            nivelAcesso char(2)
            );
        """)

        jdbcTemplate.execute("""
            create table if not exists statusColaborador(
            idStatusColaborador int primary key auto_increment,
            statusColaborador varchar(45),
            nivelAcesso char(2)
            );
        """)

        jdbcTemplate.execute("""
            create table if not exists colaborador(
            idColaborador int auto_increment,
            nome varchar(45),
            email varchar(45),
            senha varchar(45),
            CPF char(14),
            fkEmpresa int, 
            constraint fk_empresa_colabrador foreign key(fkEmpresa) references Empresa(idEmpresa),
            fkStatus int, 
            constraint fk_status_colaborador foreign key(fkStatus) references statusColaborador(idStatusColaborador),
            fkNivelAcesso int, 
            constraint fk_nivel_acesso_colaborador foreign key(fkNivelAcesso) references NivelAcesso(idNivelAcesso),
            constraint pk_composta_colaborador primary key (idColaborador, fkEmpresa, fkStatus, fkNivelAcesso)
            );
        """)

        jdbcTemplate.execute("""
            create table if not exists telefone(
            idTelefone int auto_increment,
            TelCel char(11),
            TelFixo char(10),
            fkColaborador int,
            constraint fk_colaborador_telefone foreign key(fkColaborador) references Colaborador (idColaborador),
            constraint pk_composta_telefone primary key (idTelefone, fkColaborador)
            );
        """)

        jdbcTemplate.execute("""
            create table if not exists setor(
            idSetor int primary key auto_increment,
            nome varchar(45)
            );
        """)

        jdbcTemplate.execute("""
            create table if not exists localSala(
            idLocalSala int auto_increment,
            sala int,
            andar int,
            fkSetor int,
            constraint fk_setor_local_sala foreign key(fkSetor) references setor(idSetor),
            constraint pk_composta_local_sala primary key (idLocalSala, fkSetor)
            );
        """)

        jdbcTemplate.execute("""
            create table if not exists tipoMaquina(
            idTipoMaquina int primary key auto_increment,
            tipo varchar(45)
            );
        """)

        jdbcTemplate.execute("""
            create table if not exists statusMaquina(
            idStatusMaquina int primary key auto_increment,
            statusMaquina varchar(45)
            );
        """)

        jdbcTemplate.execute("""
            create table if not exists maquina(
            idMaquina int auto_increment,
            SO varchar(45),
            IP char(9),
            fkEmpresa int, 
            constraint fk_empresa_maquina foreign key(fkEmpresa) references Empresa(idEmpresa),
            fkLocal int, 
            constraint fk_local_sala_maquina  foreign key(fkLocal) references LocalSala(idLocalSala),
            fkPlanoEmpresa int, 
            constraint fk_plano_empresa_maquina  foreign key(fkPlanoEmpresa) references Plano(idPlano),
            fkStatusMaquina int, 
            constraint fk_status_maquina  foreign key(fkStatusMaquina) references statusMaquina(idStatusMaquina),
            fkTipoMaquina int, 
            constraint fk_tipo_maquina  foreign key(fkTipoMaquina) references TipoMaquina(idTipoMaquina),
            constraint pk_composta_maquina primary key (idMaquina, fkEmpresa, fkPlanoEmpresa, fkStatusMaquina, fkTipoMaquina)
            );
        """)

        jdbcTemplate.execute("""
            create table if not exists usb(
            idUSB int auto_increment,
            nomeUSB varchar(45),
            dtHoraInserção datetime,
            fkMaquina int, 
            constraint fk_maquina_usb  foreign key(fkMaquina) references Maquina(idMaquina),
            fkEmpresa int, 
            constraint fk_empresa_usb foreign key(fkEmpresa) references Empresa(idEmpresa),
            fkPlanoEmpresa int, 
            constraint fk_plano_empresa_usb foreign key(fkPlanoEmpresa) references Plano(idPlano),
            fkTipoMaquina int, 
            constraint fk_tipo_maquina_usb foreign key(fkTipoMaquina) references TipoMaquina(idTipoMaquina),
            constraint pk_composta_usb primary key (idUSB,fkMaquina, fkEmpresa,fkPlanoEmpresa,fkTipoMaquina)
            );
        """)

        jdbcTemplate.execute("""
            create table if not exists analiseToten(
            idAnaliseToten int auto_increment,
            nomeBotao varchar(30),
            dataHora date,
            fkMaquina int, 
            constraint fk_maquina_analise_toten foreign key(fkMaquina) references Maquina(idMaquina),
            fkEmpresa int, 
            constraint fk_empresa_analise_toten foreign key(fkEmpresa) references Empresa(idEmpresa),
            fkPlanoEmpresa int, 
            constraint fk_plano_empresa_analise_toten foreign key(fkPlanoEmpresa) references Plano(idPlano),
            fkTipoMaquina int, 
            constraint fk_tipo_maquina_analise_toten foreign key(fkTipoMaquina) references TipoMaquina(idTipoMaquina),
            constraint pk_composta_analise_toten primary key (idAnaliseToten,fkMaquina, fkEmpresa,fkPlanoEmpresa,fkTipoMaquina)
            );
        """)

        jdbcTemplate.execute("""
            create table if not exists componente(
            idComponente int auto_increment,
            nome varchar(50),
            capacidade varchar(50),
            fkMaquina int, 
            constraint fk_maquina_componente foreign key(fkMaquina) references Maquina(idMaquina),
            fkEmpresaMaquina int, 
            constraint fk_empresa_maquina_componente foreign key(fkEmpresaMaquina) references Maquina(idMaquina),
            fkPlanoEmpresa int, 
            constraint fk_plano_empresa_componente foreign key(fkPlanoEmpresa) references Plano(idPlano),
            fkTipoMaquina int, 
            constraint fk_tipo_maquina_componente foreign key(fkTipoMaquina) references TipoMaquina(idTipoMaquina),
            constraint pk_composta_componete primary key (idComponente,fkMaquina, fkEmpresaMaquina,fkPlanoEmpresa,fkTipoMaquina)
            );
        """)

        jdbcTemplate.execute("""
            create table if not exists monitoramento(
            idMonitoramento int auto_increment,
            porcentagem varchar (45),
            dataHora datetime,
            fkComponente int, 
            constraint fk_componente_monitoramento foreign key(fkComponente) references Componente(idComponente),
            fkMaquina int, 
            constraint fk_maquina_monitoramento  foreign key(fkMaquina) references Maquina(idMaquina),
            fkPlanoEmpresa int, 
            constraint fk_plano_empresa_monitoramento  foreign key(fkPlanoEmpresa) references Plano(idPlano),
            fkTipoMaquina int, 
            constraint fk_tipo_maquina_monitoramento  foreign key(fkTipoMaquina) references TipoMaquina(idTipoMaquina),
            fkEmpresaMaquina int, 
            constraint fk_empresa_monitoramento  foreign key(fkEmpresaMaquina) references Maquina(idMaquina),
            constraint pk_composta_monitoramnto primary key (idMonitoramento,fkComponente,fkMaquina,  fkPlanoEmpresa, fkTipoMaquina, fkEmpresaMaquina)
            );
        """)

        jdbcTemplate.execute("""
            create table if not exists processo(
            idProcesso int  auto_increment,
            nome varchar(45),
            PID int,
            usoCPU varchar(45),
            usoRAM varchar(45),
            fkMaquina int, 
            constraint fk_maquina_processo foreign key(fkMaquina) references Maquina(idMaquina),
            fkEmpresa int, 
            constraint fk_empresa_processo foreign key(fkEmpresa) references Empresa(idEmpresa),
            fkTipoMaquina int, 
            constraint fk_tipo_maquina_processo foreign key(fkTipoMaquina) references TipoMaquina(idTipoMaquina),
            fkStatusMaquina int, 
            constraint fk_status_maquina_processo foreign key(fkStatusMaquina) references StatusMaquina(idStatusMaquina),
            constraint pk_composta_processo primary key (idProcesso, fkMaquina,  fkEmpresa, fkTipoMaquina, fkStatusMaquina)
            );
        """)

        jdbcTemplate.execute("""
            create table if not exists aviso(
            idAviso int  auto_increment,
            dataHora datetime,
            nivelAviso varchar (45),
            cor varchar (45),
            fkMonitoramento int,
             constraint foreign key(fkMonitoramento) references Monitoramento(idMonitoramento),
            fkComponente int,
            constraint fk_componete_aviso foreign key(fkComponente) references Componente(idComponente),
            fkMaquina int,
            constraint fk_maquina_aviso foreign key(fkMaquina) references Maquina(idMaquina),
            fkEmpresa int,
            constraint fk_empresa_aviso foreign key(fkEmpresa) references Empresa(idEmpresa),
            fkPlanoEmpresa int,
            constraint fk_plano_empresa_aviso foreign key(fkPlanoEmpresa) references Plano(idPlano),
            fkTipoMaquina int,
            constraint fk_tipo_maquina_aviso foreign key(fkTipoMaquina) references TipoMaquina(idTipoMaquina),
            primary key (idAviso,fkMonitoramento,fkComponente, fkMaquina,fkEmpresa, fkPlanoEmpresa, fkTipoMaquina)
            );
        """)

        jdbcTemplate.execute("""
            create table if not exists parametro(
            idParametro int auto_increment ,
            critico double,
            alerta double, 
            fkComponente int,
            constraint fk_componete_parametro foreign key(fkComponente) references Componente(idComponente),
            constraint pk_composta_parametro primary key (idParametro,fkComponente)
            );
        """)

        jdbcTemplate.execute("""
            create table if not exists monitoramento2(
            idMonitoramento2 int auto_increment,
            dataHora datetime,
            ramDisponivel varchar(20),
            ramUsada varchar(20),
            freqCpu varchar(20),
            temperatura varchar(20),
            fkComponente int, 
            constraint fk_individual_componente  foreign key(fkComponente) references Componente(idComponente),
            fkMaquina int, 
            constraint fk_individual_monitoramento  foreign key(fkMaquina) references Maquina(idMaquina),
            fkPlanoEmpresa int, 
            constraint fk_plano_empresa_individual  foreign key(fkPlanoEmpresa) references Plano(idPlano),
            fkTipoMaquina int, 
            constraint fk_tipo_maquina_individual  foreign key(fkTipoMaquina) references TipoMaquina(idTipoMaquina),
            fkEmpresaMaquina int, 
            constraint fk_empresa_individual  foreign key(fkEmpresaMaquina) references Maquina(idMaquina),
            constraint pk_composta_individual primary key (idMonitoramento,fkMaquina, fkPlanoEmpresa, fkTipoMaquina, fkEmpresaMaquina)
            );
        """)
    }

    fun iniciar(){
        jdbcTemplate = Conexao.jdbcTemplate!!
    }


    fun verificarLogin(email:String, senha:String):Int?{
        return jdbcTemplate.queryForObject("""
            select count(idColaborador) from colaborador where email = '$email' and senha = '$senha'
        """, Int::class.java)
    }

    fun dadosColaborador(email: String, senha: String):Colaborador?{
        return jdbcTemplate.queryForObject("""
            SELECT c.nome, c.fkEmpresa, c.fkNivelAcesso, e.fkPlano FROM colaborador AS c JOIN empresa AS e ON c.fkEmpresa = e.idEmpresa WHERE email = '$email' AND senha = '$senha'
        """,BeanPropertyRowMapper(Colaborador::class.java))
    }

    fun exibirMaquinas(fkEmpresa:Int?):List<Maquina>{
        return jdbcTemplate.query("""
            SELECT * FROM maquina WHERE fkEmpresa = $fkEmpresa
        """, BeanPropertyRowMapper(Maquina::class.java))
    }

    fun validarMaquina(idMaquina:Int, fkEmpresa: Int?):Int?{
        return jdbcTemplate.queryForObject("""
            SELECT COUNT(*) FROM maquina WHERE idMaquina = $idMaquina AND fkEmpresa = $fkEmpresa
        """, Int::class.java)
    }

    fun pegarDadosMaquina(idMaquina: Int):Maquina?{
        return jdbcTemplate.queryForObject("""
            SELECT * FROM maquina where idMaquina = $idMaquina
        """, BeanPropertyRowMapper(Maquina::class.java))
    }

    //talvez não precise mais disso por causa da função acima :)
    fun recuperarTipoMaquina(idMaquina:Int, fkEmpresa: Int?):Int?{
        return jdbcTemplate.queryForObject("""
            SELECT fkTipoMaquina FROM maquina WHERE idMaquina = $idMaquina AND fkEmpresa = $fkEmpresa
        """, Int::class.java)
    }

    fun cadastrarMonitoramentoCpu(monitoramento: Monitoramento2, maquina: Maquina){
        jdbcTemplate.execute("""
            insert into monitoramento2 (dataHora, freqCpu, temperatura, fkComponente, fkMaquina, fkPlanoEmpresa, fkTipoMaquina, fkEmpresaMaquina) values
            ('${monitoramento.dataHora}', '${monitoramento.freqCpu}', '${monitoramento.tempCpu}', 1, ${maquina.idMaquina}, ${maquina.fkPlanoEmpresa}, ${maquina.fkTipoMaquina}, ${maquina.fkEmpresa})
        """.trimIndent())
    }

    fun cadastrarMonitoramentoRam(monitoramento: Monitoramento2, maquina: Maquina){
        jdbcTemplate.execute("""
            insert into monitoramento2 (dataHora, ramDisponivel, ramUsada, fkComponente, fkMaquina, fkPlanoEmpresa, fkTipoMaquina, fkEmpresaMaquina) values
            ('${monitoramento.dataHora}', '${monitoramento.ramDisponivel}', '${monitoramento.ramUsada}', 2, ${maquina.idMaquina}, ${maquina.fkPlanoEmpresa}, ${maquina.fkTipoMaquina}, ${maquina.fkEmpresa})
        """.trimIndent())
    }

    fun cadastrarProcesso(processo: Processo, maquina: Maquina){
        jdbcTemplate.execute("""
           insert into processo (nome, PID, usoCPU, usoRAM, fkMaquina, fkEmpresa, fkTipoMaquina, fkStatusMaquina) values
            ('${processo.nome}', '${processo.pid}', '${processo.usoCpu}', '${processo.usoRam}', ${maquina.idMaquina}, ${maquina.fkEmpresa}, ${maquina.fkTipoMaquina}, ${maquina.fkStatusMaquina})
        """)
    }

    //

    fun cadastrarCpu(cCpu:Monitoramento){
        jdbcTemplate.execute("""
           insert into monitoramento (porcentagem, dataHora, fkComponente, fkMaquina, fkPlanoEmpresa, fkTipoMaquina, fkEmpresaMaquina) values
            ('${cCpu.porcentagem}', '${cCpu.dataHora}', 1, 1, 1, 1, 1)
        """)
    }

    fun cadastrarRam(cRam:Monitoramento){
        jdbcTemplate.execute("""
           insert into monitoramento (porcentagem, dataHora, fkComponente, fkMaquina, fkPlanoEmpresa, fkTipoMaquina, fkEmpresaMaquina) values
            ('${cRam.porcentagem}', '${cRam.dataHora}', 2, 1, 1, 1, 1)
        """)
    }



}