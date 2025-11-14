# Temenos OFS management library

A lightweight Java library for serializing and deserializing Temenos OFS messages.
OFS (Open Financial Services) messages are the text-based request/response format used to interact with Temenos Transact (T24 / Globus) core banking system.
This library provides a convenient, structured way to build, parse, and handle these proprietary message formats.

##What Are OFS Messages?

Temenos OFS messages are plain-text messages composed of field-name/value pairs. They are the primary mechanism used by external systems to send data into Temenos Transact or query data from Temenos Transact.

OFS messages serve two main purposes:

### 1. Sending Transactions/Data to Temenos

To create or update records in Temenos, you invoke a T24 application and supply field assignments in the form:

```
APPLICATION,VERSION/FUNCTION/PROCESSING_FLAG/OPTIONS,USER_INFORMATION,RECID,FIELD_VALUE_PAIRS
```

The core banking system processes the input and returns an OFS-formatted response.

2. Querying Information From Temenos (ENQUIRY.SELECT)

To retrieve data, you call a query service ENQUIRY.SELECT and provide selection criteria using the same field=value structure.

```
ENQUIRY.SELECT,,USER_INFORMATION,ENQUIRY_NAME,SELECTION_FIELD_VALUE_PAIRS
```

Temenos responds with a structured OFS message containing matching records.

## What This Library Provides

- OFS Serialization
Convert Java objects, maps, or structured input into valid OFS messages.

- OFS Deserialization
Parse incoming OFS text into Java objects or key-value structures.

- Error-safe field handling
Includes proper escaping, multi-value, sub-value support, and Temenos-style field formatting.
