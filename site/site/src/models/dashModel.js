// Começo da dash setor
var database = require('../database/config')

function setor() {
  console.log(
    "ACESSEI O USUARIO MODEL \n \n\t\t >> Se aqui der erro de 'Error: connect ECONNREFUSED',\n \t\t >> verifique suas credenciais de acesso ao banco\n \t\t >> e se o servidor de seu BD está rodando corretamente. \n\n function entrar(): "
  )
  var instrucao = `
  select
  m.idMaquina, m.fkEmpresa, m.fkPlanoEmpresa, 
  m.fkTipoMaquina, m.fkStatusMaquina, m.fkLocal, 
  l.idLocalSala, l.sala, l.andar, l.fkSetor, 
  s.nome
  from maquina as m join empresa as e on e.idEmpresa = m.fkEmpresa 
  join plano as p on e.fkPlano = p.idPlano
  join tipoMaquina as t on m.fkTipoMaquina = t.idTipoMaquina
  join statusMaquina as sm on m.fkStatusMaquina = sm.idStatusMaquina
  join localSala as l on m.fkLocal = l.idLocalSala
  join setor as s on l.fkSetor = s.idSetor
  where m.fkEmpresa = 1 and m.idMaquina = 1 
  and m.fkTipoMaquina = 1 and m.fkStatusMaquina = 1;
    `
  console.log('Executando a instrução SQL: \n' + instrucao)
  return database.executar(instrucao)
}

function buscarMaquinas() {
  console.log(
    "ACESSEI O USUARIO MODEL \n \n\t\t >> Se aqui der erro de 'Error: connect ECONNREFUSED',\n \t\t >> verifique suas credenciais de acesso ao banco\n \t\t >> e se o servidor de seu BD está rodando corretamente. \n\n function entrar(): "
  )
  var instrucao = `
  SELECT * from maquina;
  `
  console.log('Executando a instrução SQL: \n' + instrucao)
  return database.executar(instrucao)
}

//analise atual
function buscarUltimasMedidas(idMaquina) {
  instrucaoSql = ''

  if (process.env.AMBIENTE_PROCESSO == 'producao') {
    instrucaoSql = `select top ${limite_linhas}
        dht11_temperatura as temperatura, 
        dht11_umidade as umidade,  
                        momento,
                        FORMAT(momento, 'HH:mm:ss') as momento_grafico
                    from medida
                    where fk_aquario = ${idAquario}
                    order by id desc`
  } else if (process.env.AMBIENTE_PROCESSO == 'desenvolvimento') {
    instrucaoSql = `select * from monitoramento
                    where fkMaquina = ${idMaquina}
                    order by idMonitoramento desc limit 3`
  } else {
    console.log(
      '\nO AMBIENTE (produção OU desenvolvimento) NÃO FOI DEFINIDO EM app.js\n'
    )
    return
  }

  console.log('Executando a instrução SQL: \n' + instrucaoSql)
  return database.executar(instrucaoSql)
}

function buscarMedidasEmTempoReal(idMaquina) {
  instrucaoSql = ''

  if (process.env.AMBIENTE_PROCESSO == 'producao') {
    instrucaoSql = `select top 1
        dht11_temperatura as temperatura, 
        dht11_umidade as umidade,  
                        CONVERT(varchar, momento, 108) as momento_grafico, 
                        fk_aquario 
                        from medida where fk_aquario = ${idAquario} 
                    order by id desc`
  } else if (process.env.AMBIENTE_PROCESSO == 'desenvolvimento') {
    instrucaoSql = `select * from monitoramento
    where fkMaquina = ${idMaquina} 
                    order by idMonitoramento desc limit 3`
  } else {
    console.log(
      '\nO AMBIENTE (produção OU desenvolvimento) NÃO FOI DEFINIDO EM app.js\n'
    )
    return
  }

  console.log('Executando a instrução SQL: \n' + instrucaoSql)
  return database.executar(instrucaoSql)
}

//avisos
function buscarUltimosAvisos(idMaquina) {
  instrucaoSql = ''

  if (process.env.AMBIENTE_PROCESSO == 'producao') {
    instrucaoSql = `select top ${limite_linhas}
        dht11_temperatura as temperatura, 
        dht11_umidade as umidade,  
                        momento,
                        FORMAT(momento, 'HH:mm:ss') as momento_grafico
                    from medida
                    where fk_aquario = ${idAquario}
                    order by id desc`
  } else if (process.env.AMBIENTE_PROCESSO == 'desenvolvimento') {
    instrucaoSql = `
    SELECT a.idAvisos, DATE_FORMAT(a.dataHora, '%d/%m/%Y %H:%i:%s') as dtHr,
    a.fkMonitoramento, a.fkComponente, a.fkMaquina, a.fkEmpresa,
    a.fkPlanoEmpresa, a.fkTipoMaquina, a.fkNivelAviso
    FROM avisos as a join monitoramento as mt on mt.idMonitoramento = a.fkMonitoramento
    join componente as c on a.fkComponente = c.idComponente
    join maquina as m on a.fkMaquina = m.idMaquina
    join empresa as e on a.fkEmpresa = e.idEmpresa
    join plano as p on a.fkPlanoEmpresa = p.idPlano
    join tipoMaquina as t on a.fkTipoMaquina = t.idTipoMaquina
    join nivelAvisos as n on a.fkNivelAviso = n.idNivelAvisos
    where a.fkEmpresa = 1 and a.fkMaquina = ${idMaquina} and a.fkTipoMaquina = 1;`
  } else {
    console.log(
      '\nO AMBIENTE (produção OU desenvolvimento) NÃO FOI DEFINIDO EM app.js\n'
    )
    return
  }

  console.log('Executando a instrução SQL: \n' + instrucaoSql)
  return database.executar(instrucaoSql)
}

function buscarAvisosEmTempoReal(idMaquina) {
  instrucaoSql = ''

  if (process.env.AMBIENTE_PROCESSO == 'producao') {
    instrucaoSql = `select top 1
        dht11_temperatura as temperatura, 
        dht11_umidade as umidade,  
                        CONVERT(varchar, momento, 108) as momento_grafico, 
                        fk_aquario 
                        from medida where fk_aquario = ${idAquario} 
                    order by id desc`
  } else if (process.env.AMBIENTE_PROCESSO == 'desenvolvimento') {
    instrucaoSql = `    
    SELECT a.idAvisos, DATE_FORMAT(a.dataHora, '%d/%m/%Y %H:%i:%s') as dtHr,
    a.fkMonitoramento, a.fkComponente, a.fkMaquina, a.fkEmpresa,
    a.fkPlanoEmpresa, a.fkTipoMaquina, a.fkNivelAviso
    FROM avisos as a join monitoramento as mt on mt.idMonitoramento = a.fkMonitoramento
    join componente as c on a.fkComponente = c.idComponente
    join maquina as m on a.fkMaquina = m.idMaquina
    join empresa as e on a.fkEmpresa = e.idEmpresa
    join plano as p on a.fkPlanoEmpresa = p.idPlano
    join tipoMaquina as t on a.fkTipoMaquina = t.idTipoMaquina
    join nivelAvisos as n on a.fkNivelAviso = n.idNivelAvisos
    where a.fkEmpresa = 1 and a.fkMaquina = ${idMaquina} and a.fkTipoMaquina = 1;`
  } else {
    console.log(
      '\nO AMBIENTE (produção OU desenvolvimento) NÃO FOI DEFINIDO EM app.js\n'
    )
    return
  }

  console.log('Executando a instrução SQL: \n' + instrucaoSql)
  return database.executar(instrucaoSql)
}

//usb
function buscarUltimosUsb(idMaquina) {
  instrucaoSql = ''

  if (process.env.AMBIENTE_PROCESSO == 'producao') {
    instrucaoSql = `select top ${limite_linhas}
        dht11_temperatura as temperatura, 
        dht11_umidade as umidade,  
                        momento,
                        FORMAT(momento, 'HH:mm:ss') as momento_grafico
                    from medida
                    where fk_aquario = ${idAquario}
                    order by id desc`
  } else if (process.env.AMBIENTE_PROCESSO == 'desenvolvimento') {
    instrucaoSql = `select count(idUsb) as status
    from usb join maquina on maquina.idMaquina = usb.fkMaquina 
    where date(dtHoraInserção) = (SELECT CURDATE());`
  } else {
    console.log(
      '\nO AMBIENTE (produção OU desenvolvimento) NÃO FOI DEFINIDO EM app.js\n'
    )
    return
  }

  console.log('Executando a instrução SQL: \n' + instrucaoSql)
  return database.executar(instrucaoSql)
}

function buscarUsbEmTempoReal(idMaquina) {
  instrucaoSql = ''

  if (process.env.AMBIENTE_PROCESSO == 'producao') {
    instrucaoSql = `select top 1
        dht11_temperatura as temperatura, 
        dht11_umidade as umidade,  
                        CONVERT(varchar, momento, 108) as momento_grafico, 
                        fk_aquario 
                        from medida where fk_aquario = ${idAquario} 
                    order by id desc`
  } else if (process.env.AMBIENTE_PROCESSO == 'desenvolvimento') {
    instrucaoSql = `select count(idUsb) as status
    from usb join maquina on maquina.idMaquina = usb.fkMaquina 
    where date(dtHoraInserção) = (SELECT CURDATE());`
  } else {
    console.log(
      '\nO AMBIENTE (produção OU desenvolvimento) NÃO FOI DEFINIDO EM app.js\n'
    )
    return
  }

  console.log('Executando a instrução SQL: \n' + instrucaoSql)
  return database.executar(instrucaoSql)
}

//media
function buscarUltimosMedia(idMaquina) {
  instrucaoSql = ''

  if (process.env.AMBIENTE_PROCESSO == 'producao') {
    instrucaoSql = `select top ${limite_linhas}
        dht11_temperatura as temperatura, 
        dht11_umidade as umidade,  
                        momento,
                        FORMAT(momento, 'HH:mm:ss') as momento_grafico
                    from medida
                    where fk_aquario = ${idAquario}
                    order by id desc`
  } else if (process.env.AMBIENTE_PROCESSO == 'desenvolvimento') {
    instrucaoSql = `select
    (select format(avg(porcentagem),2) from monitoramento where fkComponente = 1) as usoCpuMensal,
    (select format(avg(porcentagem),2) from monitoramento where fkComponente = 2) as usoRamMensal,
    (select format(avg(porcentagem),2) from monitoramento where fkComponente = 3) as usoDiscoMensal
    from monitoramento as mt join maquina as m on  m.idMaquina = mt.fkMaquina 
    join plano as p on mt.fkPlanoEmpresa = p.idPlano
    join tipoMaquina as t on mt.fkTipoMaquina = t.idTipoMaquina
    join empresa as e on mt.fkEmpresaMaquina = e.idEmpresa
    where mt.fkMaquina = ${idMaquina} and mt.fkTipoMaquina = 1 and mt.fkEmpresaMaquina = 1
    limit 1;`
  } else {
    console.log(
      '\nO AMBIENTE (produção OU desenvolvimento) NÃO FOI DEFINIDO EM app.js\n'
    )
    return
  }

  console.log('Executando a instrução SQL: \n' + instrucaoSql)
  return database.executar(instrucaoSql)
}

function buscarMediaEmTempoReal(idMaquina) {
  instrucaoSql = ''

  if (process.env.AMBIENTE_PROCESSO == 'producao') {
    instrucaoSql = `select top 1
        dht11_temperatura as temperatura, 
        dht11_umidade as umidade,  
                        CONVERT(varchar, momento, 108) as momento_grafico, 
                        fk_aquario 
                        from medida where fk_aquario = ${idAquario} 
                    order by id desc`
  } else if (process.env.AMBIENTE_PROCESSO == 'desenvolvimento') {
    instrucaoSql = `select
    (select format(avg(porcentagem),2) from monitoramento where fkComponente = 1) as usoCpuMensal,
    (select format(avg(porcentagem),2) from monitoramento where fkComponente = 2) as usoRamMensal,
    (select format(avg(porcentagem),2) from monitoramento where fkComponente = 3) as usoDiscoMensal
    from monitoramento as mt join maquina as m on  m.idMaquina = mt.fkMaquina 
    join plano as p on mt.fkPlanoEmpresa = p.idPlano
    join tipoMaquina as t on mt.fkTipoMaquina = t.idTipoMaquina
    join empresa as e on mt.fkEmpresaMaquina = e.idEmpresa
    where mt.fkMaquina = ${idMaquina} and mt.fkTipoMaquina = 1 and mt.fkEmpresaMaquina = 1
    limit 1;`
  } else {
    console.log(
      '\nO AMBIENTE (produção OU desenvolvimento) NÃO FOI DEFINIDO EM app.js\n'
    )
    return
  }

  console.log('Executando a instrução SQL: \n' + instrucaoSql)
  return database.executar(instrucaoSql)
}

//Insight
function buscarUltimosInsight(idMaquina) {
  instrucaoSql = ''

  if (process.env.AMBIENTE_PROCESSO == 'producao') {
    instrucaoSql = `select top ${limite_linhas}
        dht11_temperatura as temperatura, 
        dht11_umidade as umidade,  
                        momento,
                        FORMAT(momento, 'HH:mm:ss') as momento_grafico
                    from medida
                    where fk_aquario = ${idAquario}
                    order by id desc`
  } else if (process.env.AMBIENTE_PROCESSO == 'desenvolvimento') {
    instrucaoSql = `SELECT
    FORMAT(
      (SELECT AVG(porcentagem) FROM (SELECT porcentagem FROM monitoramento WHERE fkComponente = 1 ORDER BY dataHora DESC LIMIT 20) AS subquery) -
      (SELECT AVG(porcentagem) FROM (SELECT porcentagem FROM monitoramento WHERE fkComponente = 1 ORDER BY dataHora DESC LIMIT 10) AS subquery)
    , 2) AS insightCpuMensal,
    FORMAT(
      (SELECT AVG(porcentagem) FROM (SELECT porcentagem FROM monitoramento WHERE fkComponente = 2 ORDER BY dataHora DESC LIMIT 20) AS subquery) -
      (SELECT AVG(porcentagem) FROM (SELECT porcentagem FROM monitoramento WHERE fkComponente = 2 ORDER BY dataHora DESC LIMIT 10) AS subquery)
    , 2) AS insightRamMensal,
    FORMAT(
      (SELECT AVG(porcentagem) FROM (SELECT porcentagem FROM monitoramento WHERE fkComponente = 3 ORDER BY dataHora DESC LIMIT 20) AS subquery) -
      (SELECT AVG(porcentagem) FROM (SELECT porcentagem FROM monitoramento WHERE fkComponente = 3 ORDER BY dataHora DESC LIMIT 10) AS subquery)
    , 2) AS insightDiscoMensal
  FROM maquina as m join monitoramento as mt on mt.fkMaquina = m.idMaquina
  join empresa as e on m.fkEmpresa = e.idEmpresa
  join statusMaquina as sm on m.fkStatusMaquina = sm.idStatusMaquina
  join tipoMaquina as t on m.fkTipoMaquina = t.idTipoMaquina
  WHERE m.idMaquina = ${idMaquina} and m.fkEmpresa = 1 and m.fkStatusMaquina = 1 and m.fkTipoMaquina = 1;`
  } else {
    console.log(
      '\nO AMBIENTE (produção OU desenvolvimento) NÃO FOI DEFINIDO EM app.js\n'
    )
    return
  }

  console.log('Executando a instrução SQL: \n' + instrucaoSql)
  return database.executar(instrucaoSql)
}

function buscarInsightEmTempoReal(idMaquina) {
  instrucaoSql = ''

  if (process.env.AMBIENTE_PROCESSO == 'producao') {
    instrucaoSql = `select top 1
        dht11_temperatura as temperatura, 
        dht11_umidade as umidade,  
                        CONVERT(varchar, momento, 108) as momento_grafico, 
                        fk_aquario 
                        from medida where fk_aquario = ${idAquario} 
                    order by id desc`
  } else if (process.env.AMBIENTE_PROCESSO == 'desenvolvimento') {
    instrucaoSql = `SELECT
    FORMAT(
      (SELECT AVG(porcentagem) FROM (SELECT porcentagem FROM monitoramento WHERE fkComponente = 1 ORDER BY dataHora DESC LIMIT 20) AS subquery) -
      (SELECT AVG(porcentagem) FROM (SELECT porcentagem FROM monitoramento WHERE fkComponente = 1 ORDER BY dataHora DESC LIMIT 10) AS subquery)
    , 2) AS insightCpuMensal,
    FORMAT(
      (SELECT AVG(porcentagem) FROM (SELECT porcentagem FROM monitoramento WHERE fkComponente = 2 ORDER BY dataHora DESC LIMIT 20) AS subquery) -
      (SELECT AVG(porcentagem) FROM (SELECT porcentagem FROM monitoramento WHERE fkComponente = 2 ORDER BY dataHora DESC LIMIT 10) AS subquery)
    , 2) AS insightRamMensal,
    FORMAT(
      (SELECT AVG(porcentagem) FROM (SELECT porcentagem FROM monitoramento WHERE fkComponente = 3 ORDER BY dataHora DESC LIMIT 20) AS subquery) -
      (SELECT AVG(porcentagem) FROM (SELECT porcentagem FROM monitoramento WHERE fkComponente = 3 ORDER BY dataHora DESC LIMIT 10) AS subquery)
    , 2) AS insightDiscoMensal
  FROM maquina as m join monitoramento as mt on mt.fkMaquina = m.idMaquina
  join empresa as e on m.fkEmpresa = e.idEmpresa
  join statusMaquina as sm on m.fkStatusMaquina = sm.idStatusMaquina
  join tipoMaquina as t on m.fkTipoMaquina = t.idTipoMaquina
  WHERE m.idMaquina = ${idMaquina} and m.fkEmpresa = 1 and m.fkStatusMaquina = 1 and m.fkTipoMaquina = 1;`
  } else {
    console.log(
      '\nO AMBIENTE (produção OU desenvolvimento) NÃO FOI DEFINIDO EM app.js\n'
    )
    return
  }

  console.log('Executando a instrução SQL: \n' + instrucaoSql)
  return database.executar(instrucaoSql)
}

//individual

//Picos
function buscarUltimosPico(idMaquina) {
  instrucaoSql = ''

  if (process.env.AMBIENTE_PROCESSO == 'producao') {
    instrucaoSql = `select top ${limite_linhas}
        dht11_temperatura as temperatura, 
        dht11_umidade as umidade,  
                        momento,
                        FORMAT(momento, 'HH:mm:ss') as momento_grafico
                    from medida
                    where fk_aquario = ${idAquario}
                    order by id desc`
  } else if (process.env.AMBIENTE_PROCESSO == 'desenvolvimento') {
    instrucaoSql = `SELECT
    (SELECT COUNT(*) FROM aviso WHERE fkComponente = 1 AND nivelAviso = 'Crítico') as picoCPU,
    (SELECT COUNT(*) FROM aviso WHERE fkComponente = 2 AND nivelAviso = 'Crítico') as picoRAM;`
  } else {
    console.log(
      '\nO AMBIENTE (produção OU desenvolvimento) NÃO FOI DEFINIDO EM app.js\n'
    )
    return
  }
  console.log('Executando a instrução SQL: \n' + instrucaoSql)
  return database.executar(instrucaoSql)
}

//Estado Ram
function buscarEstadoRam(idMaquina) {
  instrucaoSql = ''

  if (process.env.AMBIENTE_PROCESSO == 'producao') {
    instrucaoSql = `select top ${limite_linhas}
        dht11_temperatura as temperatura, 
        dht11_umidade as umidade,  
                        momento,
                        FORMAT(momento, 'HH:mm:ss') as momento_grafico
                    from medida
                    where fk_aquario = ${idAquario}
                    order by id desc`
  } else if (process.env.AMBIENTE_PROCESSO == 'desenvolvimento') {
    instrucaoSql = `SELECT ramUsada, ramDisponivel FROM monitoramento2 
    WHERE ramUsada IS NOT NULL AND ramDisponivel IS NOT NULL LIMIT 1;`
  } else {
    console.log(
      '\nO AMBIENTE (produção OU desenvolvimento) NÃO FOI DEFINIDO EM app.js\n'
    )
    return
  }
  console.log('Executando a instrução SQL: \n' + instrucaoSql)
  return database.executar(instrucaoSql)
}

//Relatorio Ram
function buscarRelatorioRam(idMaquina) {
  instrucaoSql = ''

  if (process.env.AMBIENTE_PROCESSO == 'producao') {
    instrucaoSql = `select top ${limite_linhas}
        dht11_temperatura as temperatura, 
        dht11_umidade as umidade,  
                        momento,
                        FORMAT(momento, 'HH:mm:ss') as momento_grafico
                    from medida
                    where fk_aquario = ${idAquario}
                    order by id desc`
  } else if (process.env.AMBIENTE_PROCESSO == 'desenvolvimento') {
    instrucaoSql = `SELECT
    (SELECT ROUND(AVG(porcentagem),2) FROM monitoramento WHERE fkComponente = 1) as media,
      (ROUND(((SELECT COUNT(*) FROM aviso WHERE fkComponente = 1) / (SELECT COUNT(*) FROM aviso)) * 100 ,0)) as frequencia,
      (SELECT MAX(porcentagem) FROM monitoramento WHERE fkComponente = 1) as max;`
  } else {
    console.log(
      '\nO AMBIENTE (produção OU desenvolvimento) NÃO FOI DEFINIDO EM app.js\n'
    )
    return
  }
  console.log('Executando a instrução SQL: \n' + instrucaoSql)
  return database.executar(instrucaoSql)
}

//Scatter
function plotarScatter(idMaquina) {
  instrucaoSql = ''

  if (process.env.AMBIENTE_PROCESSO == 'producao') {
    instrucaoSql = `select top ${limite_linhas}
        dht11_temperatura as temperatura, 
        dht11_umidade as umidade,  
                        momento,
                        FORMAT(momento, 'HH:mm:ss') as momento_grafico
                    from medida
                    where fk_aquario = ${idAquario}
                    order by id desc`
  } else if (process.env.AMBIENTE_PROCESSO == 'desenvolvimento') {
    instrucaoSql = `SELECT m.porcentagem AS usoCpu,
    n.temperatura AS tempCpu
    FROM monitoramento AS m JOIN monitoramento2 AS n
    ON m.dataHora = n.dataHora;`
  } else {
    console.log(
      '\nO AMBIENTE (produção OU desenvolvimento) NÃO FOI DEFINIDO EM app.js\n'
    )
    return
  }
  console.log('Executando a instrução SQL: \n' + instrucaoSql)
  return database.executar(instrucaoSql)
}

//Barra
function plotarBarra(idMaquina) {
  instrucaoSql = ''

  if (process.env.AMBIENTE_PROCESSO == 'producao') {
    instrucaoSql = `select top ${limite_linhas}
        dht11_temperatura as temperatura, 
        dht11_umidade as umidade,  
                        momento,
                        FORMAT(momento, 'HH:mm:ss') as momento_grafico
                    from medida
                    where fk_aquario = ${idAquario}
                    order by id desc`
  } else if (process.env.AMBIENTE_PROCESSO == 'desenvolvimento') {
    instrucaoSql = `SELECT
    (SELECT COUNT(*) FROM aviso WHERE fkComponente = 1 AND nivelAviso = "Crítico" AND DAYOFWEEK(dataHora) = 2) AS seg,
    (SELECT COUNT(*) FROM aviso WHERE fkComponente = 1 AND nivelAviso = "Crítico" AND DAYOFWEEK(dataHora) = 3) AS ter,
    (SELECT COUNT(*) FROM aviso WHERE fkComponente = 1 AND nivelAviso = "Crítico" AND DAYOFWEEK(dataHora) = 4) AS qua,
    (SELECT COUNT(*) FROM aviso WHERE fkComponente = 1 AND nivelAviso = "Crítico" AND DAYOFWEEK(dataHora) = 5) AS qui,
    (SELECT COUNT(*) FROM aviso WHERE fkComponente = 1 AND nivelAviso = "Crítico" AND DAYOFWEEK(dataHora) = 6) AS sex,
    (SELECT COUNT(*) FROM aviso WHERE fkComponente = 1 AND nivelAviso = "Crítico" AND DAYOFWEEK(dataHora) = 7) AS sab,
    (SELECT COUNT(*) FROM aviso WHERE fkComponente = 1 AND nivelAviso = "Crítico" AND DAYOFWEEK(dataHora) = 1) AS dom;`
  } else {
    console.log(
      '\nO AMBIENTE (produção OU desenvolvimento) NÃO FOI DEFINIDO EM app.js\n'
    )
    return
  }
  console.log('Executando a instrução SQL: \n' + instrucaoSql)
  return database.executar(instrucaoSql)
}

//Linha
function plotarLinha(idMaquina) {
  instrucaoSql = ''

  if (process.env.AMBIENTE_PROCESSO == 'producao') {
    instrucaoSql = `select top ${limite_linhas}
        dht11_temperatura as temperatura, 
        dht11_umidade as umidade,  
                        momento,
                        FORMAT(momento, 'HH:mm:ss') as momento_grafico
                    from medida
                    where fk_aquario = ${idAquario}
                    order by id desc`
  } else if (process.env.AMBIENTE_PROCESSO == 'desenvolvimento') {
    instrucaoSql = `SELECT
    (SELECT ROUND(AVG(porcentagem),2) FROM monitoramento WHERE fkComponente = 1 AND EXTRACT(HOUR FROM dataHora) = 9) AS hr1,
    (SELECT ROUND(AVG(porcentagem),2) FROM monitoramento WHERE fkComponente = 1 AND EXTRACT(HOUR FROM dataHora) = 10) AS hr2,
    (SELECT ROUND(AVG(porcentagem),2) FROM monitoramento WHERE fkComponente = 1 AND EXTRACT(HOUR FROM dataHora) = 11) AS hr3,
    (SELECT ROUND(AVG(porcentagem),2) FROM monitoramento WHERE fkComponente = 1 AND EXTRACT(HOUR FROM dataHora) = 12) AS hr4,
    (SELECT ROUND(AVG(porcentagem),2) FROM monitoramento WHERE fkComponente = 1 AND EXTRACT(HOUR FROM dataHora) = 13) AS hr5,
    (SELECT ROUND(AVG(porcentagem),2) FROM monitoramento WHERE fkComponente = 1 AND EXTRACT(HOUR FROM dataHora) = 14) AS hr6,
    (SELECT ROUND(AVG(porcentagem),2) FROM monitoramento WHERE fkComponente = 1 AND EXTRACT(HOUR FROM dataHora) = 15) AS hr7,
    (SELECT ROUND(AVG(porcentagem),2) FROM monitoramento WHERE fkComponente = 1 AND EXTRACT(HOUR FROM dataHora) = 16) AS hr8,
      (SELECT ROUND(AVG(porcentagem),2) FROM monitoramento WHERE fkComponente = 1 AND EXTRACT(HOUR FROM dataHora) = 16) AS hr9;`
  } else {
    console.log(
      '\nO AMBIENTE (produção OU desenvolvimento) NÃO FOI DEFINIDO EM app.js\n'
    )
    return
  }
  console.log('Executando a instrução SQL: \n' + instrucaoSql)
  return database.executar(instrucaoSql)
}

//ListaProcessos
function plotarListaProcessos(idMaquina) {
  instrucaoSql = ''

  if (process.env.AMBIENTE_PROCESSO == 'producao') {
    instrucaoSql = `select top ${limite_linhas}
        dht11_temperatura as temperatura, 
        dht11_umidade as umidade,  
                        momento,
                        FORMAT(momento, 'HH:mm:ss') as momento_grafico
                    from medida
                    where fk_aquario = ${idAquario}
                    order by id desc`
  } else if (process.env.AMBIENTE_PROCESSO == 'desenvolvimento') {
    instrucaoSql = `SELECT * FROM processo ORDER BY usoCPU DESC LIMIT 5;`
  } else {
    console.log(
      '\nO AMBIENTE (produção OU desenvolvimento) NÃO FOI DEFINIDO EM app.js\n'
    )
    return
  }
  console.log('Executando a instrução SQL: \n' + instrucaoSql)
  return database.executar(instrucaoSql)
}

//WordCloud
function plotarWordCloud(idMaquina) {
  instrucaoSql = ''

  if (process.env.AMBIENTE_PROCESSO == 'producao') {
    instrucaoSql = `select top ${limite_linhas}
        dht11_temperatura as temperatura, 
        dht11_umidade as umidade,  
                        momento,
                        FORMAT(momento, 'HH:mm:ss') as momento_grafico
                    from medida
                    where fk_aquario = ${idAquario}
                    order by id desc`
  } else if (process.env.AMBIENTE_PROCESSO == 'desenvolvimento') {
    instrucaoSql = `SELECT DISTINCT(nome), COUNT(*) AS quantidade FROM processo GROUP BY nome;`
  } else {
    console.log(
      '\nO AMBIENTE (produção OU desenvolvimento) NÃO FOI DEFINIDO EM app.js\n'
    )
    return
  }
  console.log('Executando a instrução SQL: \n' + instrucaoSql)
  return database.executar(instrucaoSql)
}
module.exports = {
  setor,
  buscarMaquinas,
  buscarUltimasMedidas,
  buscarMedidasEmTempoReal,
  buscarAvisosEmTempoReal,
  buscarUltimosAvisos,
  buscarUltimosUsb,
  buscarUsbEmTempoReal,
  buscarUltimosMedia,
  buscarMediaEmTempoReal,
  buscarUltimosInsight,
  buscarInsightEmTempoReal,
  buscarUltimosPico,
  buscarEstadoRam,
  buscarRelatorioRam,
  plotarScatter,
  plotarBarra,
  plotarLinha,
  plotarListaProcessos,
  plotarWordCloud
}
