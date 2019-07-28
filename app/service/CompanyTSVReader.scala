package service

import controllers._

import scala.io.Source

/**
  * Implementation of [[CompanyReader]] responsible for reading sales from a TSV file.
  *
  * @param fileName The name of the TSV file to be read.
  */
  class CompanyTSVReader(val fileName: String) extends CompanyReader {

    def readCompanies(): Seq[Company] = {
      val source = Source.fromFile(fileName)
      for {
        line <- source.getLines()
        values = line.split("\t").map(_.trim).toVector
//        cleaned_name = CompanyNameCleaner.cleaningTrailingStrategy(values(1)).toString
      } yield {
          try Company(values(0).toInt, NormalizeSupport.normalize(values(1)), NormalizeSupport.normalize(values(2)), values(3), values(4), values(5))
          catch {
            case _: ArrayIndexOutOfBoundsException => Company(values(0).toInt, NormalizeSupport.normalize(values(1)), "", "", "", "")
          }
          finally { source.close() }
      }
    }
  }

  class TruthTSVReader(val fileName: String) extends TruthReader {
    def readCompaniesTruth(): Seq[CompanyTruth] = {
      val source = Source.fromFile(fileName)
      for {
        line <- source.getLines().toVector
        values = line.split("\t").map(_.trim)
      } yield CompanyTruth(values(0).toInt, values(1).toInt, "")
    }
  }
