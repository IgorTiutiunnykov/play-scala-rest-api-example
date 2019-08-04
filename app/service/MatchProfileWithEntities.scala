//package service
//
//import java.io._
//
//import models._
//import org.apache.commons.lang3.StringUtils
//
//case class MissedMatches(idProfiles: Int, idEntities: Int, profName: String, entName: String, profUrl: String, entUrl: String, scoreName: Double, scoreUrl: Double)
//
//object MatchProfileWithEntities {
//  def main(args: Array[String]): Unit = {
//
//    def similarText(text1: String, text2: String): Boolean = {
//      val maxLength = Math.max(text1.length, text2.length).toFloat
//      val levenshteinDist = StringUtils.getLevenshteinDistance(text1, text2).toFloat
//      (1.0 - levenshteinDist / maxLength >= 0.90)
//    }
//
//    def similarText1(text1: String, text2: String): Double = {
//      val maxLength = Math.max(text1.length, text2.length).toFloat
//      val levenshteinDist = StringUtils.getLevenshteinDistance(text1, text2).toFloat
//      val score: Double = maxLength match {
//        case _ if (maxLength == 0) => 0.0
//        case _ => 1.0 - levenshteinDist / maxLength
//      }
//
//      BigDecimal(score).setScale(1, BigDecimal.RoundingMode.HALF_UP).toDouble
//    }
//
//    println("Prepare to read")
//
//    val compEntities = new CompanyTSVReader("company_entities.tsv").readCompanies()
//    val compProfiles = new CompanyTSVReader("company_profiles.tsv").readCompanies()
//    val compTruth = new TruthTSVReader("ground_truth.tsv").readCompaniesTruth()
//
//    //use the map method on the collection to convert it into a collection of tuples and then use the : _* trick to convert the result into a variable argument
//    val compProfilesMap = Map(compProfiles map { s => (s.normalizedName, s) }: _*)
//    val compEntitiesMap = Map(compEntities map { s => (s.normalizedName, s) }: _*)
//
//    val list = compEntities.map(a => a.normalizedName)
//
//    println("Reading is done")
//
//        def matchComp(): Seq[MatchedCompanies] = for {
//          compEntity <- compEntities
//          compProfile <- compProfiles
//          if (compEntity.normalizedName == compProfile.normalizedName
//            || (!compEntity.websiteUrl.isEmpty && compEntity.websiteUrl == compProfile.websiteUrl && compEntity.country == compProfile.country))}
//          yield MatchedCompanies(compProfile.id, compEntity.id, compEntity.companyName)
////
////    def matchComp(): Seq[MatchedCompanies] = for {
////      compEntity <- compEntities
////      compProfile <- compProfiles
////      if (similarText(compEntity.normalizedName, compProfile.normalizedName)
////        || (compEntity.websiteUrl == compProfile.websiteUrl && compEntity.country == compProfile.country && !compEntity.websiteUrl.isEmpty))}
////      yield MatchedCompanies(compProfile.id, compEntity.id, compEntity.companyName)
//
//    //    def matchComp(): Seq[MatchedCompanies] = for {
//    //      compEntity <- compEntities
//    //      (k, v) <- compProfilesMap
//    //      if (compEntity.normalizedName == k
//    //        || (compEntity.websiteUrl == v.websiteUrl && compEntity.country == v.country && !compEntity.websiteUrl.isEmpty))}
//    //      yield MatchedCompanies(v.id, compEntity.id, compEntity.companyName)
//
//    val matchedComps = matchComp
//
//    def getMatchedCompanies(amount: Int) = matchedComps.take(amount)
//
//    def similar(e: MatchedCompanies, f: MatchedCompanies) = {
//      e.idProfiles == f.idProfiles && e.idEntities == f.idEntities
//    }
//
//    val test = compTruth.filter(aa => {
//      !matchedComps.exists { bb => similar(aa, bb) }
//    })
//
//    val false_pos = matchedComps.filter(aa => {
//      !compTruth.exists { bb => similar(aa, bb) }
//    })
//
//    def matchByName(e: MatchedCompanies, name: String) = {
//      e.name == name
//    }
//
//    def getMatches(name: String) = matchedComps.filter { aa =>
//      matchByName(aa, name)
//    }
//
//    def matchById(e: Company, id: Int) = {
//      e.id == id
//    }
//
//    val testMatch = getMatches("KiwiSecurity Software GmbH")
//
//
//    val prepareMissedMatches: Seq[MissedMatches] =
//      for {entry <- false_pos
//           profName = compProfiles.filter(aa => matchById(aa, entry.idProfiles)).head.normalizedName
//           entName = compEntities.filter(aa => matchById(aa, entry.idEntities)).head.normalizedName
//           profUrl = compProfiles.filter(aa => matchById(aa, entry.idProfiles)).head.websiteUrl
//           entUrl = compEntities.filter(aa => matchById(aa, entry.idEntities)).head.websiteUrl
//      } yield MissedMatches(entry.idProfiles, entry.idEntities,
//        profName,
//        entName,
//        profUrl,
//        entUrl,
//        similarText1(profName, entName),
//        similarText1(profUrl, entUrl)
//      )
//
//    // write results to file
////    val file = new File("missed_matches.tsv")
////    val bw = new BufferedWriter(new FileWriter(file))
////    for {
////      line <- prepareMissedMatches
////    }
////      bw.write(line.idProfiles + "\t" + line.idEntities + "\t" + line.profName + "\t" + line.entName +
////        "\t" + line.profUrl + "\t" + line.entUrl + "\t" + line.scoreName + "\n")
////    bw.close()
//
//    val file1 = new File("false_matches.tsv")
//    val bw1 = new BufferedWriter(new FileWriter(file1))
//    for {
//      line <- prepareMissedMatches
//    }
//      bw1.write(line.idProfiles + "\t" + line.idEntities + "\t" + line.profName + "\t" + line.entName +
//        "\t" + line.profUrl + "\t" + line.entUrl + "\t" + line.scoreUrl + "\n")
//    bw1.close()
//
//
//    println("Done")
//  }
//
//}
