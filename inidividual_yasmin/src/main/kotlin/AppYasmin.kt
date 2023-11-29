import com.github.britooo.looca.api.core.Looca
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import javax.swing.JOptionPane
import kotlin.math.pow

fun main() {
    val repositorio = Repositorio()
    repositorio.iniciar()
    repositorio.criarTabelas()

    fazerLogin()
}

fun fazerLogin(){
    print("***** Boas Vindas! *****")
    val sn = Scanner(System.`in`)

    print("\r\nDigite o email: ")
    val email = sn.next()
    print("Digite a senha: ")
    val senha = sn.next()

//    val email:String = JOptionPane.showInputDialog("Digite o email")
//    val senha:String = JOptionPane.showInputDialog("Digite a senha")

    val repositorio = Repositorio()
    repositorio.iniciar()
    val verifica = repositorio.verificarLogin(email, senha)

    if (verifica == 1){
        println("\r\n***** Login realizado com sucesso! *****")
//        exibirMensagem("***** Login realizado com sucesso! *****")
        aposLogin(email, senha)
    } else {
        println("\r\n***** Login inválido! *****\r\n")
//        exibirMensagem("***** Login inválido! *****")
    }
}

fun aposLogin(email:String,senha:String){
    val repositorio = Repositorio()
    repositorio.iniciar()

    val colaborador = repositorio.dadosColaborador(email,senha)
    val nomeColaborador = colaborador?.nome
    val fkEmpresa = colaborador?.fkEmpresa

    val cargoColaborador = when (colaborador?.fkNivelAcesso){
        1 -> "Representante Legal"
        2 -> "Gerente de TI"
        3 -> "Equipe de TI"
        else -> "N/A"
    }

    println("\r\nOlá, $cargoColaborador ${nomeColaborador}!")
//    exibirMensagem("Olá, $cargoColaborador ${nomeColaborador}!")
    escolherMaquina(fkEmpresa)
}

fun escolherMaquina(fkEmpresa:Int?){
    val repositorio = Repositorio()
    repositorio.iniciar()
    val sn = Scanner(System.`in`)

    println("Exibindo lista de máquinas cadastradas!\r\n")
    val listaMaquinas = repositorio.exibirMaquinas(fkEmpresa)
    listaMaquinas.forEach{
        println("""
                ID: ${it.idMaquina}
                SO: ${it.so}
                IP: ${it.ip}

            """.trimIndent())
    }

//    exibirMensagem("Exibindo lista de máquinas cadastradas!")
//    val listaMaquinas = repositorio.exibirMaquinas(fkEmpresa)
//    listaMaquinas.forEach{
//        exibirMensagem("""
//                ID: ${it.idMaquina}
//                SO: ${it.so}
//                IP: ${it.ip}
//
//            """.trimIndent())
//    }

    print("Digite o ID da máquina que deseja monitorar: ")
    val idMaquina = sn.next()

//    val idMaquina = JOptionPane.showInputDialog("Digite o ID da máquina que deseja monitorar")

    val validacaoMaquina = repositorio.validarMaquina(idMaquina.toInt(), fkEmpresa)

    if (validacaoMaquina == 1){
//        exibirMensagem("***** Iniciando Monitoramento *****")
        val maquina = Maquina()
        val dadoMaquina = repositorio.pegarDadosMaquina(idMaquina.toInt())

        maquina.idMaquina = dadoMaquina?.idMaquina!!
        maquina.fkPlanoEmpresa = dadoMaquina.fkPlanoEmpresa
        maquina.fkTipoMaquina = dadoMaquina.fkTipoMaquina
        maquina.fkEmpresa = dadoMaquina.fkEmpresa
        maquina.fkStatusMaquina = dadoMaquina.fkStatusMaquina

//               val tipoMaquina = repositorio.recuperarTipoMaquina(idMaquina.toInt(), fkEmpresa)
//        println(tipoMaquina)

        cadastrarMonitoramento(idMaquina.toInt())
    } else{
        println("\r\n***** Máquina não encontrada! *****")
//        exibirMensagem("***** Máquina não encontrada! *****")
    }
}

fun cadastrarMonitoramento(idMaquina:Int){
    while (true){
        println("\r\nIniciando Monitoramento: ${LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))}\r\n")
        val looca = Looca()
        val repositorio = Repositorio()
        repositorio.iniciar()
        val monitoramento = Monitoramento()
        val dtHr = LocalDateTime.now()

        val maquina = Maquina()
        val dadoMaquina = repositorio.pegarDadosMaquina(idMaquina)

        monitoramento.dataHora = dtHr.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))

        maquina.idMaquina = dadoMaquina?.idMaquina!!
        maquina.fkPlanoEmpresa = dadoMaquina.fkPlanoEmpresa
        maquina.fkTipoMaquina = dadoMaquina.fkTipoMaquina
        maquina.fkEmpresa = dadoMaquina.fkEmpresa
        maquina.fkStatusMaquina = dadoMaquina.fkStatusMaquina

        val processador = looca.processador
        monitoramento.porcentagemCpu = "%.2f".format(processador.uso).replace(",", ".").toDouble()
        println("Uso da CPU: ${"%.1f".format(processador.uso).replace(",",".")}%\r\n")

        val ram = looca.memoria
        val ramEmUso = ram.emUso.toDouble()
        val ramTotal = ram.total.toDouble()
        val percRam = (ramEmUso/ramTotal) * 100
        monitoramento.porcentagemRam = "%.2f".format(percRam).replace(",", ".").toDouble()
        println("Uso da RAM: ${"%.1f".format(percRam).replace(",",".")}%\r\n")

        val ramFree = looca.memoria.disponivel
        monitoramento.ramDisponivel = "${"%.2f".format((ramFree.toDouble() / 1024.0.pow(3.0)) * 1024).replace(",",".")}MB"
        println("Total de RAM Disponível: ${"%.2f".format((ramFree.toDouble() / 1024.0.pow(3.0)) * 1024).replace(",",".")}MB\r\n")

        val ramUsed = looca.memoria.emUso
        monitoramento.ramUsada = "${"%.2f".format((ramUsed.toDouble() / 1024.0.pow(3.0)) * 1024).replace(",",".")}MB"
        println("Total de RAM Usada: ${"%.2f".format((ramUsed.toDouble() / 1024.0.pow(3.0)) * 1024).replace(",",".")}MB\r\n")

        val frequencia = looca.processador.frequencia
        monitoramento.frequencia = "${frequencia.toDouble() / 1000000}MHz"
        println("Frequência da CPU: ${frequencia.toDouble() / 1000000}MHz\r\n")

        //Isso aq não funciona aaaaaaa
        //val temperatura = looca.temperatura
        //monitoramento.temperatura = "${"%.1f".format(temperatura.temperatura).replace(",",".")}%"
        //println("Temperatura da CPU: ${"%.1f".format(temperatura.temperatura).replace(",",".")}%\r\n")

        println("******************************************************************")

        repositorio.cadastrarMonitoramentoCpu(monitoramento, maquina)
        repositorio.cadastrarMonitoramentoRam(monitoramento, maquina)

//        val processos = looca.grupoDeProcessos
//        val listaProcessos = processos.processos
//        val processo = Processo()
//        for (p in listaProcessos) {
//            processo.pid = p.pid
//            processo.nome = p.nome
//            processo.usoCpu = "%.2f".format(p.usoCpu).replace(",", ".")
//            processo.usoRam = "%.2f".format(p.usoMemoria).replace(",", ".")
////            repositorio.cadastrarProcesso(processo, maquina)
//        }
        Thread.sleep(5000)
    }
}

//fun exibirMensagem(mensagem:String){
//    JOptionPane.showMessageDialog(null, mensagem)
//}