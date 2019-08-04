package service

import models._

import scala.io.Source
import scala.util.Try

trait CompanyReader {
  def readCompanies(): Vector[Company]
}

trait TruthReader {
  def readCompaniesTruth(): Vector[MatchedCompanies]
}

class CompanyTSVReader(val fileName: String) extends CompanyReader {
  def readCompanies(): Vector[Company] = {
    val bufferedSource = Source.fromFile(fileName)
    val result =
      (for {line <- bufferedSource.getLines()
            values = line.split("\t").map(_.trim)
            id = values(0).toInt
            name = values(1)
            websiteUrl = Try(Normalizer.webUrl(values(2))).getOrElse("")
            foundationYear = Try(values(3)).getOrElse("")
            city = Try(values(4)).getOrElse("")
            country = Try(values(5)).getOrElse("")
            normalizedName = Normalizer.companyName(name)
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
    bufferedSource.close
    result
  }
}
