//// LifestyleFactorsController.java
//@RestController
//@RequestMapping("/lifestyle")
//public class LifestyleFactorsController {
//
//   // Open to all for reading.
//   @GetMapping
//   public ResponseEntity<List<LifestyleFactor>> getLifestyleFactors() {
//      // Return data accessible by both admin and guest.
//   }
//
//   // Restricted to admin only for writing.
//   @PostMapping
//   @PreAuthorize("hasRole('ADMIN')")
//   public ResponseEntity<LifestyleFactor> createLifestyleFactor(@RequestBody LifestyleFactor factor) {
//      // Only an admin can create a new lifestyle factor.
//   }
//
//   // Similarly, restrict PUT/DELETE operations.
//   @PutMapping("/{id}")
//   @PreAuthorize("hasRole('ADMIN')")
//   public ResponseEntity<LifestyleFactor> updateLifestyleFactor(@PathVariable Long id, @RequestBody LifestyleFactor factor) {
//      // Update only allowed for admin.
//   }
//
//   @DeleteMapping("/{id}")
//   @PreAuthorize("hasRole('ADMIN')")
//   public ResponseEntity<Void> deleteLifestyleFactor(@PathVariable Long id) {
//      // Deletion only allowed for admin.
//   }
//}
