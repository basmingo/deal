package ru.neoflex.deal.model

case class Credit(creditId: Option[Integer],
                  term: Integer,
                  monthlyPayment: java.math.BigDecimal,
                  rate: java.math.BigDecimal,
                  psk: Option[java.math.BigDecimal],
                  withInsurance: Boolean,
                  salaryClient: Boolean,
                  creditStatus: String)
