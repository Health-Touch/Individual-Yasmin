// Começo da dash setor
var express = require('express')
var router = express.Router()

var dashController = require('../controllers/dashController')

//Recebendo os dados do html e direcionando para a função cadastrar de usuarioController.js
router.get('/setor', function (req, res) {
  dashController.setor(req, res)
})

router.get('/maquinas', function (req, res) {
  dashController.buscarMaquinas(req, res)
})

//Analise atual
router.get('/ultimas/:idMaquina', function (req, res) {
  dashController.buscarUltimasMedidas(req, res)
})

router.get('/tempo-real/:idMaquina', function (req, res) {
  dashController.buscarMedidasEmTempoReal(req, res)
})

//Avisos
router.get('/ultimos/:idMaquina', function (req, res) {
  dashController.buscarUltimosAvisos(req, res)
})

router.get('/avisos/tempo-real/:idMaquina', function (req, res) {
  dashController.buscarAvisosEmTempoReal(req, res)
})

//Usb
router.get('/usb/:idMaquina', function (req, res) {
  dashController.buscarUltimosUsb(req, res)
})

router.get('/usb/tempo-real/:idMaquina', function (req, res) {
  dashController.buscarUsbEmTempoReal(req, res)
})

//Média
router.get('/media/:idMaquina', function (req, res) {
  dashController.buscarUltimosMedia(req, res)
})

router.get('/media/tempo-real/:idMaquina', function (req, res) {
  dashController.buscarMediaEmTempoReal(req, res)
})

//Insight
router.get('/insight/:idMaquina', function (req, res) {
  dashController.buscarUltimosInsight(req, res)
})

router.get('/insight/tempo-real/:idMaquina', function (req, res) {
  dashController.buscarInsightEmTempoReal(req, res)
})

//Fazendo o individual aq
//Picos
router.get('/pico/:idMaquina', function (req, res) {
  dashController.buscarUltimosPico(req, res)
})

//Estado Ram
router.get('/estadoRam/:idMaquina', function (req, res) {
  dashController.buscarEstadoRam(req, res)
})

//Relatorio Ram
router.get('/relatorioRam/:idMaquina', function (req, res) {
  dashController.buscarRelatorioRam(req, res)
})

//Scatter
router.get('/scatter/:idMaquina', function (req, res) {
  dashController.plotarScatter(req, res)
})

//Barra
router.get('/barra/:idMaquina', function (req, res) {
  dashController.plotarBarra(req, res)
})

//Linha
router.get('/linha/:idMaquina', function (req, res) {
  dashController.plotarLinha(req, res)
})

//ListaProcessos
router.get('/listaProcessos/:idMaquina', function (req, res) {
  dashController.plotarListaProcessos(req, res)
})

//WordCloud
router.get('/wordCloud/:idMaquina', function (req, res) {
  dashController.plotarWordCloud(req, res)
})

module.exports = router
