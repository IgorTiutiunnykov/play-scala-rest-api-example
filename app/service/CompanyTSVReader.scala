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
            name = values(1)
            websiteUrl = Try(NormalizeSupport.normalize(values(2))).getOrElse("")
            foundationYear = Try(NormalizeSupport.normalize(values(3))).getOrElse("")
            city = Try(NormalizeSupport.normalize(values(4))).getOrElse("")
            country = Try(NormalizeSupport.normalize(values(5))).getOrElse("")
            normalizedName = NormalizeSupport.normalize(name)

      }
        yield Company(id, name, websiteUrl, foundationYear, city, country, normalizedName)).toVector
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


