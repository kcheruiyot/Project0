import com.kipngetich.dbmcqs.cli.Cli
import com.kipngetich.dbmcqs.utils.ReadFileUtil
import com.kipngetich.dbmcqs.dao.MCQDao
object Main{
  def main(args:Array[String]):Unit = {
    val cli = new Cli()
    ReadFileUtil.readJson()
    MCQDao.readWriteJson()
    cli.openCli()
  }
}

