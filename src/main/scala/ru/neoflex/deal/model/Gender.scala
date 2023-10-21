package ru.neoflex.deal.model

sealed trait Gender

case class MALE() extends Gender

case class FEMALE() extends Gender

case class NON_BINARY() extends Gender

case class TRANSGENDER() extends Gender

case class DIFFERENT() extends Gender
