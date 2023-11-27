import java.time.LocalDateTime

class Monitoramento {
    var idMonitoramento:Int = 0
    var porcentagem:String = ""
    var dataHora:LocalDateTime = LocalDateTime.now()
    var fkComponente:Int = 0
    var fkMaquina:Int = 0
    var fkPlanoEmpresa:Int = 0
    var fkTipoMaquina:Int = 0
    var fkEmpresaMaquina:Int = 0
}