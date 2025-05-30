/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
import org.junit.jupiter.api.Test

import java.util.regex.Matcher
import java.util.regex.Pattern

import static groovy.test.GroovyAssert.assertScript

final class OperatorsTest {

    @Test
    void testArithmeticOperators() {
        // tag::binary_arith_ops[]
        assert  1  + 2 == 3
        assert  4  - 3 == 1
        assert  3  * 5 == 15
        assert  3  / 2 == 1.5
        assert 10  % 3 == 1
        assert  2 ** 3 == 8
        // end::binary_arith_ops[]

        // tag::unary_plus_minus[]
        assert +3 == 3
        assert -4 == 0 - 4

        assert -(-1) == 1  // <1>
        // end::unary_plus_minus[]

        // tag::plusplus_minusminus[]
        def a = 2
        def b = a++ * 3             // <1>

        assert a == 3 && b == 6

        def c = 3
        def d = c-- * 2             // <2>

        assert c == 2 && d == 6

        def e = 1
        def f = ++e + 3             // <3>

        assert e == 2 && f == 5

        def g = 4
        def h = --g + 1             // <4>

        assert g == 3 && h == 4
        // end::plusplus_minusminus[]
    }

    @Test
    void testArithmeticOperatorsWithAssignment() {
        // tag::binary_assign_operators[]
        def a = 4
        a += 3

        assert a == 7

        def b = 5
        b -= 3

        assert b == 2

        def c = 5
        c *= 3

        assert c == 15

        def d = 10
        d /= 2

        assert d == 5

        def e = 10
        e %= 3

        assert e == 1

        def f = 3
        f **= 2

        assert f == 9
        // end::binary_assign_operators[]
    }

    @Test
    void testSimpleRelationalOperators() {
        // tag::simple_relational_op[]
        assert 1 + 2 == 3
        assert 3 != 4

        assert -2 < 3
        assert 2 <= 2
        assert 3 <= 4

        assert 5 > 1
        assert 5 >= -2
        // end::simple_relational_op[]
    }

    @Test
    void testLogicalOperators() {
        // tag::logical_op[]
        assert !false           // <1>
        assert true && true     // <2>
        assert true || false    // <3>
        // end::logical_op[]
    }

    @Test
    void testBitwiseOperators() {
        // tag::bitwise_op[]
        int a = 0b00101010
        assert a == 42
        int b = 0b00001000
        assert b == 8
        assert (a & a) == a                     // <1>
        assert (a & b) == b                     // <2>
        assert (a | a) == a                     // <3>
        assert (a | b) == a                     // <4>

        int mask = 0b11111111                   // <5>
        assert ((a ^ a) & mask) == 0b00000000   // <6>
        assert ((a ^ b) & mask) == 0b00100010   // <7>
        assert ((~a) & mask)    == 0b11010101   // <8>
        // end::bitwise_op[]
    }

    @Test
    void testBitShiftOperators() {
        // tag::bit_shift_op[]
        assert 12.equals(3 << 2)           // <1>
        assert 24L.equals(3L << 3)         // <1>
        assert 48G.equals(3G << 4)         // <1>

        assert 4095 == -200 >>> 20
        assert -1 == -200 >> 20
        assert 2G == 5G >> 1
        assert -3G == -5G >> 1
        // end::bit_shift_op[]
    }

    @Test
    void testLogicalOperatorPrecedence() {
        // tag::logical_precendence_1[]
        assert (!false && false) == false   // <1>
        // end::logical_precendence_1[]

        // tag::logical_precendence_2[]
        assert true || true && false        // <1>
        // end::logical_precendence_2[]
    }

    @Test
    void testLogicalShortCircuit() {
        assertScript '''
            // tag::logical_shortcircuit[]
            boolean checkIfCalled() {   // <1>
                called = true
            }

            called = false
            true || checkIfCalled()
            assert !called              // <2>

            called = false
            false || checkIfCalled()
            assert called               // <3>

            called = false
            false && checkIfCalled()
            assert !called              // <4>

            called = false
            true && checkIfCalled()
            assert called               // <5>
            // end::logical_shortcircuit[]
        '''
    }

    @Test
    void testConditionalOperators() {
        // tag::conditional_op_not[]
        assert (!true)    == false                      // <1>
        assert (!'foo')   == false                      // <2>
        assert (!'')      == true                       // <3>
        // end::conditional_op_not[]
        def result
        def string = 'some string'
        // tag::conditional_op_ternary_if[]
        if (string!=null && string.length()>0) {
            result = 'Found'
        } else {
            result = 'Not found'
        }
        // end::conditional_op_ternary_if[]
        assert result == 'Found'
        result = null
        // tag::conditional_op_ternary_ternary[]
        result = (string!=null && string.length()>0) ? 'Found' : 'Not found'
        // end::conditional_op_ternary_ternary[]
        assert result == 'Found'

        // tag::conditional_op_ternary_groovytruth[]
        result = string ? 'Found' : 'Not found'
        // end::conditional_op_ternary_groovytruth[]
        assert result == 'Found'

        def user = [name: 'Bob']
        def displayName
        // tag::conditional_op_elvis[]
        displayName = user.name ? user.name : 'Anonymous'   // <1>
        displayName = user.name ?: 'Anonymous'              // <2>
        // end::conditional_op_elvis[]
    }

    private record Person(long id, String name) {
        static Person find(Closure<?> c) { null }
    }

    @Test
    void testNullSafeOperator() {
        // tag::nullsafe[]
        def person = Person.find { it.id == 123 }    // <1>
        def name = person?.name                      // <2>
        assert name == null                          // <3>
        // end::nullsafe[]

        // GROOVY-11591
        boolean called
        def f = { -> called = true }
        def obj = null
        assert !called
        obj?.grep(f())
        assert !called
        obj?[f()]
        assert !called
    }

    @Test
    void testDirectFieldAccess() {
        assertScript '''
// tag::direct_field_class[]
class User {
    public final String name                 // <1>
    User(String name) { this.name = name}
    String getName() { "Name: $name" }       // <2>
}
def user = new User('Bob')
assert user.name == 'Name: Bob'              // <3>
// end::direct_field_class[]
// tag::direct_field_op[]
assert user.@name == 'Bob'                   // <1>
// end::direct_field_op[]
'''
    }

    @Test
    void testMethodPointer() {
        // tag::method_pointer[]
        def str = 'example of method reference'            // <1>
        def fun = str.&toUpperCase                         // <2>
        def upper = fun()                                  // <3>
        assert upper == str.toUpperCase()                  // <4>
        // end::method_pointer[]
        assert fun instanceof Closure

        assertScript '''
            class Person {
                String name
                int age
            }
            // tag::method_pointer_strategy[]
            def transform(List elements, Closure action) {                    // <1>
                def result = []
                elements.each {
                    result << action(it)
                }
                result
            }
            String describe(Person p) {                                       // <2>
                "$p.name is $p.age"
            }
            def action = this.&describe                                       // <3>
            def list = [
                new Person(name: 'Bob',   age: 42),
                new Person(name: 'Julia', age: 35)]                           // <4>
            assert transform(list, action) == ['Bob is 42', 'Julia is 35']    // <5>

            // end::method_pointer_strategy[]
        '''

        assertScript '''
            // tag::method_pointer_dispatch[]
            def doSomething(String str) { str.toUpperCase() }    // <1>
            def doSomething(Integer x) { 2*x }                   // <2>
            def reference = this.&doSomething                    // <3>
            assert reference('foo') == 'FOO'                     // <4>
            assert reference(123)   == 246                       // <5>
            // end::method_pointer_dispatch[]
        '''

        assertScript '''
            // tag::method_pointer_new[]
            def foo  = BigInteger.&new
            def fortyTwo = foo('42')
            assert fortyTwo == 42G
            // end::method_pointer_new[]
        '''

        assertScript '''
            // tag::method_pointer_class_instance[]
            def instanceMethod = String.&toUpperCase
            assert instanceMethod('foo') == 'FOO'
            // end::method_pointer_class_instance[]
        '''
    }

    @Test
    void testMethodReference() {
        assertScript '''
            // tag::method_refs[]
            import groovy.transform.CompileStatic
            import static java.util.stream.Collectors.toList

            @CompileStatic
            void methodRefs() {
                assert 6G == [1G, 2G, 3G].stream().reduce(0G, BigInteger::add)                           // <1>

                assert [4G, 5G, 6G] == [1G, 2G, 3G].stream().map(3G::add).collect(toList())              // <2>

                assert [1G, 2G, 3G] == [1L, 2L, 3L].stream().map(BigInteger::valueOf).collect(toList())  // <3>

                assert [1G, 2G, 3G] == [1L, 2L, 3L].stream().map(3G::valueOf).collect(toList())          // <4>
            }

            methodRefs()
            // end::method_refs[]
            // tag::constructor_refs[]
            @CompileStatic
            void constructorRefs() {
                assert [1, 2, 3] == ['1', '2', '3'].stream().map(Integer::valueOf).collect(toList())  // <1>

                def result = [1, 2, 3].stream().toArray(Integer[]::new)                           // <2>
                assert result instanceof Integer[]
                assert result.toString() == '[1, 2, 3]'
            }

            constructorRefs()
            // end::constructor_refs[]
        '''
    }

    @Test
    void testRegularExpressionOperators() {
        def pattern = 'foo'
        // tag::pattern_op[]
        def p = ~/foo/
        assert p instanceof Pattern
        // end::pattern_op[]
        // tag::pattern_op_variants[]
        p = ~'foo'                                                        // <1>
        p = ~"foo"                                                        // <2>
        p = ~$/dollar/slashy $ string/$                                   // <3>
        p = ~"${pattern}"                                                 // <4>
        // end::pattern_op_variants[]

        // tag::pattern_matcher_op[]
        def text = "some text to match"
        def m = text =~ /match/                                           // <1>
        assert m instanceof Matcher                                       // <2>
        if (!m) {                                                         // <3>
            throw new RuntimeException("Oops, text not found!")
        }
        // end::pattern_matcher_op[]

        // tag::pattern_matcher_strict_op[]
        m = text ==~ /match/                                              // <1>
        assert m instanceof Boolean                                       // <2>
        if (m) {                                                          // <3>
            throw new RuntimeException("Should not reach that point!")
        }
        // end::pattern_matcher_strict_op[]

        // tag::pattern_find_vs_matcher[]
        assert 'two words' ==~ /\S+\s+\S+/
        assert 'two words' ==~ /^\S+\s+\S+$/         // <1>
        assert !(' leading space' ==~ /\S+\s+\S+/)   // <2>

        def m1 = 'two words' =~ /^\S+\s+\S+$/
        assert m1.size() == 1                          // <3>
        def m2 = 'now three words' =~ /^\S+\s+\S+$/    // <4>
        assert m2.size() == 0                          // <5>
        def m3 = 'now three words' =~ /\S+\s+\S+/
        assert m3.size() == 1                          // <6>
        assert m3[0] == 'now three'
        def m4 = ' leading space' =~ /\S+\s+\S+/
        assert m4.size() == 1                          // <7>
        assert m4[0] == 'leading space'
        def m5 = 'and with four words' =~ /\S+\s+\S+/
        assert m5.size() == 2                          // <8>
        assert m5[0] == 'and with'
        assert m5[1] == 'four words'
        // end::pattern_find_vs_matcher[]
    }

    @Test
    void testSpreadDotOperator() {
        assertScript '''
// tag::spreaddot[]
class Car {
    String make
    String model
}
def cars = [
       new Car(make: 'Peugeot', model: '508'),
       new Car(make: 'Renault', model: 'Clio')]       // <1>
def makes = cars*.make                                // <2>
assert makes == ['Peugeot', 'Renault']                // <3>
// end::spreaddot[]
// tag::spreaddot_nullsafe[]
cars = [
   new Car(make: 'Peugeot', model: '508'),
   null,                                              // <1>
   new Car(make: 'Renault', model: 'Clio')]
assert cars*.make == ['Peugeot', null, 'Renault']     // <2>
assert null*.make == null                             // <3>
// end::spreaddot_nullsafe[]
'''

        assertScript '''
// tag::spreaddot_iterable[]
class Component {
    Integer id
    String name
}
class CompositeObject implements Iterable<Component> {
    def components = [
        new Component(id: 1, name: 'Foo'),
        new Component(id: 2, name: 'Bar')]

    @Override
    Iterator<Component> iterator() {
        components.iterator()
    }
}
def composite = new CompositeObject()
assert composite*.id == [1,2]
assert composite*.name == ['Foo','Bar']
// end::spreaddot_iterable[]
'''

        assertScript '''
import groovy.transform.Canonical

// tag::spreaddot_multilevel[]
class Make {
    String name
    List<Model> models
}

@Canonical
class Model {
    String name
}

def cars = [
    new Make(name: 'Peugeot',
             models: [new Model('408'), new Model('508')]),
    new Make(name: 'Renault',
             models: [new Model('Clio'), new Model('Captur')])
]

def makes = cars*.name
assert makes == ['Peugeot', 'Renault']

def models = cars*.models*.name
assert models == [['408', '508'], ['Clio', 'Captur']]
assert models.sum() == ['408', '508', 'Clio', 'Captur'] // flatten one level
assert models.flatten() == ['408', '508', 'Clio', 'Captur'] // flatten all levels (one in this case)
// end::spreaddot_multilevel[]
'''

        assertScript '''
// tag::spreaddot_alternative[]
class Car {
    String make
    String model
}
def cars = [
   [
       new Car(make: 'Peugeot', model: '408'),
       new Car(make: 'Peugeot', model: '508')
   ], [
       new Car(make: 'Renault', model: 'Clio'),
       new Car(make: 'Renault', model: 'Captur')
   ]
]
def models = cars.collectNested{ it.model }
assert models == [['408', '508'], ['Clio', 'Captur']]
// end::spreaddot_alternative[]
'''
    }

    @Test
    void testSpreadMethodArguments() {
        assertScript '''
// tag::spreadmethodargs_method[]
int function(int x, int y, int z) {
    x*y+z
}
// end::spreadmethodargs_method[]
// tag::spreadmethodargs_args[]
def args = [4,5,6]
// end::spreadmethodargs_args[]
// tag::spreadmethodargs_assert[]
assert function(*args) == 26
// end::spreadmethodargs_assert[]
// tag::spreadmethodargs_mixed[]
args = [4]
assert function(*args,5,6) == 26
// end::spreadmethodargs_mixed[]
'''
    }

    @Test
    void testSpreadList() {
        // tag::spread_list[]
        def items = [4,5]                      // <1>
        def list = [1,2,3,*items,6]            // <2>
        assert list == [1,2,3,4,5,6]           // <3>
        // end::spread_list[]
    }

    @Test
    void testSpreadMap() {
        assertScript '''
        // tag::spread_map[]
        def m1 = [c:3, d:4]                   // <1>
        def map = [a:1, b:2, *:m1]            // <2>
        assert map == [a:1, b:2, c:3, d:4]    // <3>
        // end::spread_map[]
        '''

        assertScript '''
        // tag::spread_map_position[]
        def m1 = [c:3, d:4]                   // <1>
        def map = [a:1, b:2, *:m1, d: 8]      // <2>
        assert map == [a:1, b:2, c:3, d:8]    // <3>
        // end::spread_map_position[]
        '''
    }

    @Test
    void testRangeOperator() {
        assertScript '''
        // tag::intrange[]
        def range = 0..5                                    // <1>
        assert (0..5).collect() == [0, 1, 2, 3, 4, 5]       // <2>
        assert (0..<5).collect() == [0, 1, 2, 3, 4]         // <3>
        assert (0<..5).collect() == [1, 2, 3, 4, 5]         // <4>
        assert (0<..<5).collect() == [1, 2, 3, 4]           // <5>
        assert (0..5) instanceof List                       // <6>
        assert (0..5).size() == 6                           // <7>
        // end::intrange[]
        '''

        assertScript '''
        // tag::charrange[]
        assert ('a'..'d').collect() == ['a','b','c','d']
        // end::charrange[]
        '''
    }

    @Test
    void testSpaceshipOperator() {
        assertScript '''
        // tag::spaceship[]
        assert (1 <=> 1) == 0
        assert (1 <=> 2) == -1
        assert (2 <=> 1) == 1
        assert ('a' <=> 'z') == -1
        // end::spaceship[]
        '''
    }

    @Test
    void testSubscriptOperator() {
        assertScript '''
        // tag::subscript_op[]
        def list = [0,1,2,3,4]
        assert list[2] == 2                         // <1>
        list[2] = 4                                 // <2>
        assert list[0..2] == [0,1,4]                // <3>
        list[0..2] = [6,6,6]                        // <4>
        assert list == [6,6,6,3,4]                  // <5>
        // end::subscript_op[]
        '''

        assertScript '''
        // tag::subscript_destructuring[]
        class User {
            Long id
            String name
            def getAt(int i) {                                             // <1>
                switch (i) {
                    case 0: return id
                    case 1: return name
                }
                throw new IllegalArgumentException("No such element $i")
            }
            void putAt(int i, def value) {                                 // <2>
                switch (i) {
                    case 0: id = value; return
                    case 1: name = value; return
                }
                throw new IllegalArgumentException("No such element $i")
            }
        }
        def user = new User(id: 1, name: 'Alex')                           // <3>
        assert user[0] == 1                                                // <4>
        assert user[1] == 'Alex'                                           // <5>
        user[1] = 'Bob'                                                    // <6>
        assert user.name == 'Bob'                                          // <7>
        // end::subscript_destructuring[]
        '''
    }

    @Test
    void testMembershipOperator() {
        // tag::membership_op[]
        def list = ['Grace','Rob','Emmy']
        assert ('Emmy' in list)                     // <1>
        assert ('Alex' !in list)                    // <2>
        // end::membership_op[]
    }

    @Test
    void testIdentityOperator() {
        // tag::identity_op[]
        def list1 = ['Groovy 1.8','Groovy 2.0','Groovy 2.3']        // <1>
        def list2 = ['Groovy 1.8','Groovy 2.0','Groovy 2.3']        // <2>
        assert list1 == list2                                       // <3>
        assert !list1.is(list2)                                     // <4>
        assert list1 !== list2                                      // <5>
        // end::identity_op[]
    }

    @Test
    void testCoercionOperator() {
        try {
            // tag::coerce_op_cast[]
            String input = '42'
            Integer num = (Integer) input                      // <1>
            // end::coerce_op_cast[]
            assert false, 'Should not reach here but instead should have thrown a ClassCastException'
        } catch (ClassCastException e) {
            assert e.message == "Cannot cast object '42' with class 'java.lang.String' to class 'java.lang.Integer'"
            // tag::coerce_op[]
            String input = '42'
            Integer num = input as Integer                      // <1>
            // end::coerce_op[]
            assert num == 42
        }

        assertScript '''
        // tag::coerce_op_custom[]
        class Identifiable {
            String name
        }
        class User {
            Long id
            String name
            def asType(Class target) {                                              // <1>
                if (target == Identifiable) {
                    return new Identifiable(name: name)
                }
                throw new ClassCastException("User cannot be coerced into $target")
            }
        }
        def u = new User(name: 'Xavier')                                            // <2>
        def p = u as Identifiable                                                   // <3>
        assert p instanceof Identifiable                                            // <4>
        assert !(p instanceof User)                                                 // <5>
        // end::coerce_op_custom[]
        '''
    }

    @Test
    void testDiamondOperator() {
        // tag::diamond_op[]
        List<String> strings = new LinkedList<>()
        // end::diamond_op[]
    }

    @Test
    void testCallOperator() {
        assertScript '''
        // tag::call_op[]
        class MyCallable {
            int call(int x) {           // <1>
                2*x
            }
        }

        def mc = new MyCallable()
        assert mc.call(2) == 4          // <2>
        assert mc(2) == 4               // <3>
        // end::call_op[]
        '''
    }

    @Test
    void testOperatorOverloading() {
        assertScript '''
// tag::operator_overload_class[]
class Bucket {
    int size

    Bucket(int size) { this.size = size }

    Bucket plus(Bucket other) {                     // <1>
        return new Bucket(this.size + other.size)
    }
}
// end::operator_overload_class[]
// tag::operator_overload_op[]
def b1 = new Bucket(4)
def b2 = new Bucket(11)
assert (b1 + b2).size == 15                         // <1>
// end::operator_overload_op[]
'''
    }

    @Test
    void testOperatorOverloadingWithDifferentArgumentType() {
        assertScript '''
class Bucket {
    int size

    Bucket(int size) { this.size = size }

// tag::operator_overload_mixed_class[]
    Bucket plus(int capacity) {
        return new Bucket(this.size + capacity)
    }
// end::operator_overload_mixed_class[]
}
def b1 = new Bucket(4)
// tag::operator_overload_mixed_op[]
assert (b1 + 11).size == 15
// end::operator_overload_mixed_op[]
'''
    }

    @Test
    void testGStringEquals() {
        assertScript '''
            w = 'world'
            str1 = "Hello $w"
            str1 += "!"
            str2 = "Hello $w!"
            str3 = 'Hello world!'

            assert str1 == str3
            assert str2 == str3
            assert str1 == str2
            '''
    }

    @Test
    void testBooleanOr() {
        assertScript '''
boolean trueValue1 = true, trueValue2 = true, trueValue3 = true
boolean falseValue1 = false, falseValue2 = false, falseValue3 = false

assert (trueValue1 |= true)
assert (trueValue2 |= false)
assert (trueValue3 |= null)
assert (falseValue1 |= true)
assert !(falseValue2 |= false)
assert !(falseValue3 |= null)
'''
    }

    @Test
    void testBooleanAnd() {
        assertScript '''
boolean trueValue1 = true, trueValue2 = true, trueValue3 = true
boolean falseValue1 = false, falseValue2 = false, falseValue3 = false

assert (trueValue1 &= true)
assert !(trueValue2 &= false)
assert !(trueValue3 &= null)
assert !(falseValue1 &= true)
assert !(falseValue2 &= false)
assert !(falseValue3 &= null)
'''
    }

    @Test
    void testBooleanXor() {
        assertScript '''
boolean trueValue1 = true, trueValue2 = true, trueValue3 = true
boolean falseValue1 = false, falseValue2 = false, falseValue3 = false

assert !(trueValue1 ^= true)
assert (trueValue2 ^= false)
assert (trueValue3 ^= null)
assert (falseValue1 ^= true)
assert !(falseValue2 ^= false)
assert !(falseValue3 ^= null)
'''
    }
}
