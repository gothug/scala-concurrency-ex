package ch1

import org.scalatest._
import Solutions._

/**
 * @author Got Hug
 */
class SolutionsTest extends WordSpec with Matchers {
  "fuse" should {
    "work correctly" in {
      fuse(None, None) shouldBe None
      fuse(Some(1), None) shouldBe None
      fuse(None, Some(2)) shouldBe None
      fuse(Some(1), Some(2)) shouldBe Some((1, 2))
    }
  }

  "fuseViaMap" should {
    "work correctly" in {
      fuseViaMap(None, None) shouldBe None
      fuseViaMap(Some(1), None) shouldBe None
      fuseViaMap(None, Some(2)) shouldBe None
      fuseViaMap(Some(1), Some(2)) shouldBe Some((1, 2))
    }
  }

  "check" should {
    "work correctly" in {
      check(0 until 10)(40 / _ > 0) shouldBe false
      check(1 until 10)(40 / _ > 0) shouldBe true
    }
  }

  "permutations" should {
    "work correctly" in {
      permutations("abc").size shouldBe 6
    }
  }
}
