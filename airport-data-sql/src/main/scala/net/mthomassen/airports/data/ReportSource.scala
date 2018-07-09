package net.mthomassen.airports.data

trait ReportSource {

  def report(report: Report.Value): Tabular

}
