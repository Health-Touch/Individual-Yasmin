import psutil as ps
import time as tm
from datetime import datetime as dt
import pyodbc

# Realizando a conexão com o BD
conn = pyodbc.connect(
    'DRIVER={SQL Server};' 'SERVER=54.145.218.19;'
    'DATABASE=HealthTouch;' 'UID=sa;' 'PWD=urubu100'
)
cursor = conn.cursor()

# Pedindo dados de login do usuário
print("***** Boas Vindas! *****")
email = input("Digite seu email: ")
senha = input("Digite sua senha: ")

# Fazendo a validação do usuário no banco
query = "SELECT * FROM Colaborador WHERE email = ? AND senha = ?"
login = (email, senha,)

cursor.execute(query, login)
validacao_login = cursor.fetchone()

# Se a validação do usuário estiver ok
if validacao_login:
    print("\n***** Login realizado com sucesso! *****")

    # Segunda consulta para pegar dados específicos do usuário e exibir
    query = """
    SELECT c.nome, c.fkEmpresa, c.fkNivelAcesso, e.fkPlano
    FROM Colaborador AS c
    JOIN Empresa AS e ON c.fkEmpresa = e.idEmpresa
    WHERE email = ? AND senha = ?
    """
    login = (email, senha,)
    cursor.execute(query, login)

    dados_colaborador = cursor.fetchall()

    # Aqui ele pega os dados do banco e guarda nas variáveis
    for dado in dados_colaborador:
        nome_colaborador = dado[0]
        fk_empresa = dado[1]
        cargo_colaborador = dado[2]
        plano_empresa = dado[3]

        # Validação do cargo do usuário
        if cargo_colaborador == 1:
            cargo_colaborador = "Representante Legal"
        elif cargo_colaborador == 2:
            cargo_colaborador = "Gerente de TI"
        else:
            cargo_colaborador = "Equipe de TI"

        print(f"Olá, {cargo_colaborador} {nome_colaborador}!")

    # Consulta para pegar as máquinas cadastradas na empresa do usuário
    print("\nExibindo lista de máquinas cadastradas!\n")
    query = "SELECT * FROM Maquina WHERE fkEmpresa = ?"
    cursor.execute(query, (fk_empresa,))
    lista_maquinas = cursor.fetchall()

    # Exibindo as máquinas cadastradas
    for dado in lista_maquinas:
        print("ID:", dado[0])
        print("SO:", dado[1])
        print("IP:", dado[2])
        print()

    # Pegando o ID da máquina que será monitorada
    print("*" * 50)
    id_maquina = input("Digite o ID da máquina que deseja monitorar: ")

    # Verificando se o ID da máquina existe
    query = "SELECT * FROM Maquina WHERE idMaquina = ? AND fkEmpresa = ?"
    cursor.execute(query, (id_maquina, fk_empresa,))
    validacao_maquina = cursor.fetchone()

    # Pegando o tipo da máquina
    query = "SELECT fkTipoMaquina FROM Maquina WHERE idMaquina = ? AND fkEmpresa = ?"
    cursor.execute(query, (id_maquina, fk_empresa,))
    tipo_maquina = cursor.fetchall()
    for i in tipo_maquina:
        fk_tipo_maquina = i[0]

    # Se a validação da máquina estiver ok
    if validacao_maquina:
        # Monitoramento
        while True:
            # Porcentagem da CPU
            uso_cpu = ps.cpu_percent(interval=None, percpu=False)

            # Porcentagem da RAM
            uso_ram = ps.virtual_memory().percent

            # Tempo total de uso da máquina - provavelmente não vou usar.
            uso_maquina = round(tm.time() - ps.boot_time())

            # Uso da RAM
            total_ram = ps.virtual_memory().total / (1024 ** 3)  # GB
            aval_ram = ps.virtual_memory().available / (1024 ** 3)  # livre em GB
            used_ram = ps.virtual_memory().used / (1024 ** 3)  # uso atual em GB
            free_ram = ps.virtual_memory().free / (1024 ** 3)  # livre em GB

            # Convertendo em MegaByte
            total_ram = total_ram * 1024
            aval_ram = aval_ram * 1024
            used_ram = used_ram * 1024

            # Frequência da CPU
            freq_cpu = ps.cpu_freq(percpu=False).current

            # Uso máximo da CPU - esse não precisa tbm
            freq_max_cpu = ps.cpu_freq(percpu=False).max

            # Temperatura do HW - Linux - simplemente não vai :)
            # temp = ps.sensors_temperatures(fahrenheit=False)

            # data_hora = dt.now()
            data_hora = "2023-11-03 11:42:37"

            # Prints
            print("Iniciando Monitoramento:", format(dt.now(), "%d/%m/%Y %H:%M:%S"), "\n")
            print(f"Uso da CPU: {uso_cpu}%")
            print(f"\nUso da RAM: {uso_ram}%")
            # print("\nTempo Total de Uso da Máquina:",uso_maquina)
            print("\nTotal da RAM: {:.2f}MB".format(total_ram))
            print("\nTotal de Ram Disponível: {:.2f}MB".format(aval_ram))
            print("\nTotal de Ram Usada: {:.2f}MB".format(used_ram))
            print("\nTotal de Ram Livre: {:.2f}MB".format(free_ram))
            print(f"\nFrequência da CPU: {freq_cpu}MHz")
            print(f"\nUso Máximo da CPU: {freq_max_cpu}MHz\n")
            # print("\nTemperatura:",temp)
            print("*" * 50)

            # Inserindo valores no banco
            # De CPU
            query = """
            INSERT INTO MonitoramentoYasmin (porcentagem, dataHora, frequencia, fkComponente, fkMaquina, fkPlanoEmpresa, fkTipoMaquina, fkEmpresaMaquina)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            """
            monitoramento = (uso_cpu, data_hora, f"{freq_cpu}MHz", 1, id_maquina, plano_empresa, fk_tipo_maquina, fk_empresa)

            cursor.execute(query, monitoramento)
            conn.commit()

            # De RAM
            query2 = """
            INSERT INTO MonitoramentoYasmin (porcentagem, dataHora, ramDisponivel, ramUsada, fkComponente, fkMaquina, fkPlanoEmpresa, fkTipoMaquina, fkEmpresaMaquina)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
            """
            monitoramento = (uso_ram, data_hora, "{:.2f}MB".format(aval_ram), "{:.2f}MB".format(used_ram), 2, id_maquina, plano_empresa, fk_tipo_maquina, fk_empresa)

            cursor.execute(query2, monitoramento)
            conn.commit()

            tm.sleep(5)  # Tempo de execução em segundos
    else:
        print("\n***** Máquina não encontrada! *****")
else:
    print("\n***** Login inválido! *****")
    cursor.close()
