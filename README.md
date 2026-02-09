# Distributed Key-Value Store

A distributed key-value store inspired by Amazon Dynamo and LinkedIn's Voldemort, implementing leaderless replication with eventual consistency.

## Overview

This project explores the core concepts of distributed data storage systems used in production at scale. The system prioritizes **availability** and **partition tolerance** over strict consistency, using techniques like consistent hashing, quorum-based operations, and vector clocks for conflict resolution.

## Architecture Goals

- **Leaderless replication:** No single point of failure, all nodes are peers
- **Tunable consistency:** Configurable N (replication factor), R (read quorum), W (write quorum)
- **Horizontal scalability:** Add/remove nodes with minimal data movement
- **Conflict resolution:** Detect and resolve concurrent writes using vector clocks
- **Fault tolerance:** Continue operating during node failures

## Learning Resources

This implementation is guided by foundational research and production system designs:

### Core Papers & Books

**Primary References:**
- Designing Data-Intensive Applications by Martin Kleppmann
  - Chapter 5: Replication (focus on leaderless replication)
  - Chapter 6: Partitioning (consistent hashing, virtual nodes)
  - Chapter 7: Transactions (conflict resolution, vector clocks)
  
- Dynamo: Amazon's Highly Available Key-value Store
  - Section 4: System Architecture
  - Section 4.3: Replication
  - Section 4.4: Data Versioning (vector clocks)
  - Section 4.7: Handling Failures (hinted handoff, read repair)

**Implementation References:**
- Project Voldemort Design
  - LinkedIn's production Dynamo-inspired system
  - Consistent hashing implementation
  - Vector clock implementation
  - Client-side routing

### Consensus & Distributed Systems Background

**Understanding Raft (for context, NOT used in this project):**
- The Secret Lives of Data - Raft Visualization
  - Interactive visualization of leader-based consensus
  - Useful for understanding the *alternative* approach (leader-based vs leaderless)
  
- Raft Paper
  - Understanding strong consistency and leader election
  - Contrast with our eventual consistency model  

## Key Design Decisions

### Consistency Model
- **Eventual consistency** over strong consistency
- Tunable via N/R/W parameters
- Example: N=3, R=2, W=2 ensures read-after-write consistency

### Partitioning Strategy
- **Consistent hashing** with virtual nodes
- Minimizes data movement when adding/removing nodes
- Even load distribution across cluster

### Conflict Resolution
- **Vector clocks** for causality tracking
- Application-level conflict resolution
- Last-write-wins as fallback

### Failure Handling
- **Sloppy quorum:** Accept writes even when preferred nodes are down
- **Hinted handoff:** Temporary storage on alternative nodes
- **Read repair:** Fix inconsistencies during reads

## Running the System
```bash
