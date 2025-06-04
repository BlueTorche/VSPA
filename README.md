## Visibly System of Procedural Automata validating JSON documents

The project implement a Master Thesis using VSPA to validate JSON documents against a JSON schema. 

A VSPA is an extension of finite automata that can mutually call each other. We do not expand here about the formal behavior of a VSPA, and we refer to _Compositional learning of mutually recursive procedural systems_, 2021, Frohme et al. for a formal description of SPA, which are the foundation of the VSPA. 

The code is implemented in Java 23. 

## VSPA 

The package ``src.vspa`` handle the foundation of general VSPA, with a validation function according to the language of the VSPA. The class ``src.test.testVSPA.java`` shows example of usage of VSPA. 

## JSON Validation

The package ``src.json_vspa`` contains the classes used to perform the validation of VSPA. It contains an extension of VSPA using key graph that can perform the validation of object schema in arbitrary order of key value pair, without enumerating all possible permutation. We refer to _Validating Streaming JSON Documents with Learned VPAs_, TACAS 2023, Bruyère, Pérez and Staquet, for the intuition of how we handle unordered object with the key graph.

The class ``src.test.testJSONVSPA.java`` shows example of usage of VSPA in order to validate a JSON document against a JSON schema. The class ``src.test.TestResult.java`` perform the validation of real JSON document against real JSON schema, and saves the metrics of computation time and memory usage. The schema and the documents come from a [repository validating JSON using VPAs](https://github.com/DocSkellington/ValidatingJSONDocumentsWithLearnedVPA/).
