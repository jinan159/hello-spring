package hello.hellospring.repository;

import hello.hellospring.domain.Member;

import java.util.*;

public class MemoryMemberRepository implements MemberRepository{

    // 클래스에서 공유되는 변수는, 동시성을 제어하기 위해서 실무에서는 'java.util.concurrent.ConcurrentHashMap'을 사용한다.
    private static Map<Long, Member> store = new HashMap<>();

    // 이 변수도, 동시성 제어를 위해서는 'java.util.concurrent.atomic.AtomicLong'을 사용해야 한다.
    private static long sequence = 0L;

    @Override
    public Member save(Member member) {
        member.setId(++sequence);
        store.put(member.getId(), member);
        return member;
    }

    @Override
    public Optional<Member> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Optional<Member> findByName(String name) {
        return store.values().stream()
                .filter(member -> member.getName().equals(name))
                .findAny();
    }

    @Override
    public List<Member> findAll() {
        return new ArrayList<>(store.values());
    }

    public void clearStore() {
        store.clear();
    }
}
