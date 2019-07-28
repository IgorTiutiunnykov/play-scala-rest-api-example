package service

import scala.io.Source
import scala.util.Try

/**
  * Implementation of [[CompanyReader]] and [[TruthReader]] responsible for reading companies data from a TSV file.
  *
  * @param fileName The name of the TSV file to be read.
  */
class CompanyTSVReader(val fileName: String) extends CompanyReader {
  def readCompanies(): Vector[Company] = {
    val bufferedSource = Source.fromFile(fileName)
    val result =
      (for {line <- bufferedSource.getLines()
            values = line.split("\t").map(_.trim)
            id = values(0).toInt
            name = NormalizeSupport.normalize(values(1))
            website_url = Try(NormalizeSupport.normalize(values(2))).getOrElse("")
            foundation_year = Try(NormalizeSupport.normalize(values(3))).getOrElse("")
            city = Try(NormalizeSupport.normalize(values(4))).getOrElse("")
            country = Try(NormalizeSupport.normalize(values(5))).getOrElse("")
      }
        yield Company(id, name, website_url, foundation_year, city, country)).toVector
    bufferedSource.close
    result
  }
}

class TruthTSVReader(val fileName: String) extends TruthReader {
  def readCompaniesTruth(): Vector[MatchedCompanies] = {
    val bufferedSource = Source.fromFile(fileName)
    val result =
      (for {
        line <- bufferedSource.getLines()
        values = line.split("\t").map(_.trim)
      } yield MatchedCompanies(values(0).toInt, values(1).toInt, "")).toVector
    bufferedSource.close()
    result
  }
}


