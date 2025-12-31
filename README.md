# post-quantum-certificate-generator


## About

Private utility project made open-source.
Java command line tool generating X.509 certificates based on quantum secure cryptography (following the NIST standards).

This has been created as a part of my post-quantum cryptography R&D, presently supporting only the plain Dilithium3 certificates, which was required to my needs. It's a POC version. If time allows I plan to extend it with other NIST-approved algorithms, supported by BouncyCastle & the ability to generate hybrid certificates.

Recommended only for concept phase & research purposes. You use it on your own liability and risk.


## Usage

Build the jar with `MainPQC.java` as a main file, creating an artifact: `pqcg.jar`.

Provide a json-formatted request in the `request-name.json`.
Provide an output directory `target-directory`.

Call: `java -jar pqcg.jar request-name.json target-directory`

Under the target-directory I'll obtain a whole set consisting of:
- request-name.csr
- request-name.key  // private key
- request-name.pub  // public key
- request-name.crt


## Required Build Dependencies

- bcpkix-jdk18on-1.81.jar
- bcprov-jdk18on-1.81.jar
- bcutil-jdk18on-1.81.jar
- jackson-annotations-2.19.1.jar
- jackson-core-2.19.1.jar
- jackson-databind-2.19.1.jar


## TODOs

Project is still in a POC stage. What's still required:
- gradle/mvn dependency management
- CI/CD pipeline & build automation
- CVEs scanning
- Other quantum secure algorithms support
- options support
