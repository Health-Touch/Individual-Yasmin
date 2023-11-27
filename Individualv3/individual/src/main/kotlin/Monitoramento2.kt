import java.time.LocalDateTime

class Monitoramento2 {
    var idMonitoramento:Int = 0
    var dataHora: LocalDateTime = LocalDateTime.now()
    var ramDisponivel:String = ""
    var ramUsada:String = ""
    var freqCpu:String = ""
    var tempCpu:String = ""
    var fkComponente:Int = 0
    var fkMaquina:Int = 0
    var fkPlanoEmpresa:Int = 0
    var fkTipoMaquina:Int = 0
    var fkEmpresaMaquina:Int = 0
}