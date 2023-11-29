import org.apache.commons.dbcp2.BasicDataSource
import org.springframework.jdbc.core.JdbcTemplate

object Conexao {
    //MYSQL
//    var jdbcTemplate: JdbcTemplate? = null
//        get() {
//            if(field == null){
//                val dataSource = BasicDataSource()
//                dataSource.driverClassName = "com.mysql.cj.jdbc.Driver"
//                dataSource.url = "jdbc:mysql://3.88.218.112:3306/HealthTouch"
//                dataSource.username = "root"
//                dataSource.password = "urubu100"
//                val novoJdbcTemplate = JdbcTemplate(dataSource)
//                field = novoJdbcTemplate
//            }
//            return field
//        }

    //SQL SERVER
    var jdbcTemplate: JdbcTemplate? = null
        get() {
            if(field == null){
                val dataSource = BasicDataSource()
                dataSource.driverClassName = "com.microsoft.sqlserver.jdbc.SQLServerDriver"
                dataSource.url = "jdbc:sqlserver://54.145.218.19:1433;databaseName=HealthTouch;encrypt=false"
                dataSource.username = "sa"
                dataSource.password = "urubu100"
                val novoJdbcTemplate = JdbcTemplate(dataSource)
                field = novoJdbcTemplate
            }
            return field
        }
}