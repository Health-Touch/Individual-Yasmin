import java.time.LocalDateTime

class Monitoramento {
    var idMonitoramento:Int = 0
    var porcentagemCpu:Double = 0.0
    var porcentagemRam:Double = 0.0
    var dataHora:String = ""
    var ramDisponivel:String = ""
    var ramUsada:String = ""
    var frequencia:String = ""
    var fkComponente:Int = 0
    var fkMaquina:Int = 0
    var fkPlanoEmpresa:Int = 0
    var fkTipoMaquina:Int = 0
    var fkEmpresaMaquina:Int = 0
}