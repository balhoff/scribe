package scribe

import scribe.format.FormatBlock.RawString
import scribe.output.{Color, ColoredOutput}

import scala.language.experimental.macros

package object format {
  private val ThreadNameAbbreviationLength = 10
  private val PositionAbbreviationLength = 25

  def string(value: String): FormatBlock = RawString(value)
  def date: FormatBlock = FormatBlock.Date.Standard
  def timeStamp: FormatBlock = FormatBlock.TimeStamp
  def time: FormatBlock = FormatBlock.Time
  def threadName: FormatBlock = FormatBlock.ThreadName
  def threadNameAbbreviated: FormatBlock = threadName.abbreviate(
    maxLength = ThreadNameAbbreviationLength,
    padded = true,
    abbreviateName = true
  )
  def level: FormatBlock = FormatBlock.Level
  def levelColored: FormatBlock = FormatBlock { logRecord =>
    val color = logRecord.level match {
      case Level.Trace => Color.White
      case Level.Debug => Color.Green
      case Level.Info => Color.Blue
      case Level.Warn => Color.Yellow
      case Level.Error => Color.Red
      case _ => Color.Cyan
    }
    new ColoredOutput(color, level.format(logRecord))
  }
  def levelPaddedRight: FormatBlock = FormatBlock.Level.PaddedRight
  def levelColoredPaddedRight: FormatBlock = FormatBlock { logRecord =>
    val color = logRecord.level match {
      case Level.Trace => Color.White
      case Level.Debug => Color.Green
      case Level.Info => Color.Blue
      case Level.Warn => Color.Yellow
      case Level.Error => Color.Red
      case _ => Color.Cyan
    }
    new ColoredOutput(color, FormatBlock.Level.PaddedRight.format(logRecord))
  }
  def fileName: FormatBlock = FormatBlock.FileName
  def line: FormatBlock = FormatBlock.LineNumber
  def column: FormatBlock = FormatBlock.ColumnNumber
  def position: FormatBlock = FormatBlock.Position
  def positionAbbreviated: FormatBlock = position.abbreviate(
    maxLength = PositionAbbreviationLength,
    padded = true,
    abbreviateName = true
  )
  def message: FormatBlock = FormatBlock.Message
  def newLine: FormatBlock = FormatBlock.NewLine
  def mdc(key: String): FormatBlock = FormatBlock.MDCReference(key)
  def mdc: FormatBlock = FormatBlock.MDCAll

  def colored(color: Color, block: FormatBlock): FormatBlock = FormatBlock { logRecord =>
    new ColoredOutput(color, block.format(logRecord))
  }
  def black(block: FormatBlock): FormatBlock = colored(Color.Black, block)
  def blue(block: FormatBlock): FormatBlock = colored(Color.Blue, block)
  def cyan(block: FormatBlock): FormatBlock = colored(Color.Cyan, block)
  def green(block: FormatBlock): FormatBlock = colored(Color.Green, block)
  def magenta(block: FormatBlock): FormatBlock = colored(Color.Magenta, block)
  def red(block: FormatBlock): FormatBlock = colored(Color.Red, block)
  def white(block: FormatBlock): FormatBlock = colored(Color.White, block)
  def yellow(block: FormatBlock): FormatBlock = colored(Color.Yellow, block)
  def gray(block: FormatBlock): FormatBlock = colored(Color.Gray, block)
  def brightBlue(block: FormatBlock): FormatBlock = colored(Color.BrightBlue, block)
  def brightCyan(block: FormatBlock): FormatBlock = colored(Color.BrightCyan, block)
  def brightGreen(block: FormatBlock): FormatBlock = colored(Color.BrightGreen, block)
  def brightMagenta(block: FormatBlock): FormatBlock = colored(Color.BrightMagenta, block)
  def brightRed(block: FormatBlock): FormatBlock = colored(Color.BrightRed, block)
  def brightWhite(block: FormatBlock): FormatBlock = colored(Color.BrightWhite, block)
  def brightYellow(block: FormatBlock): FormatBlock = colored(Color.BrightYellow, block)

  implicit class FormatterInterpolator(val sc: StringContext) extends AnyVal {
    def formatter(args: Any*): Formatter = macro ScribeMacros.formatter
  }
}
