package controllers

import javax.swing.text.html.parser.Entity

import scala.io.Source

object ReadData {
  def main(args: Array[String]): Unit = {

    println("Prepare to read")


    val compEntities = new CompanyTSVReader("company_entities.tsv").readCompanies()
    val compProfiles = new CompanyTSVReader("company_profiles.tsv").readCompanies()
    val compTruth = new TruthTSVReader("ground_truth.tsv").readCompaniesTruth()


    def matchComp(): Seq[CompanyTruth] = for {
      compEntity <- compEntities
      compProfile <- compProfiles
      if (compEntity.company_name == compProfile.company_name || (compEntity.website_url == compProfile.website_url) && !compEntity.website_url.isEmpty)}
    yield CompanyTruth(compProfile.id, compEntity.id, compProfile.company_name)

    println("Done1")

    val matchedComps = matchComp

    def similar(e: CompanyTruth, f: CompanyTruth) = { e.id_profiles == f.id_profiles && e.id_entities == f.id_entities}

    val test = compTruth.filter(aa => {! matchedComps.exists {bb => similar (aa, bb)} })


    println("Done")
  }

}
