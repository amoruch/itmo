
import behavioral.chain.ChainOfResponsibilityDemo;
import behavioral.command.CommandDemo;
import behavioral.interpreter.InterpreterDemo;
import behavioral.iterator.IteratorDemo;
import behavioral.mediator.MediatorDemo;
import behavioral.memento.MementoDemo;
import behavioral.nullobject.NullObjectDemo;
import behavioral.observer.ObserverDemo;
import behavioral.state.StateDemo;
import behavioral.strategy.StrategyDemo;
import behavioral.templatemethod.TemplateMethodDemo;
import behavioral.visitor.VisitorDemo;
import creational.abstractfactory.AbstractFactoryDemo;
import creational.builder.BuilderDemo;
import creational.factorymethod.FactoryMethodDemo;
import creational.objectpool.ObjectPoolDemo;
import creational.prototype.PrototypeDemo;
import creational.singleton.SingletonDemo;
import structural.adapter.AdapterDemo;
import structural.bridge.BridgeDemo;
import structural.composite.CompositeDemo;
import structural.decorator.DecoratorDemo;
import structural.facade.FacadeDemo;
import structural.flyweight.FlyweightDemo;
import structural.proxy.ProxyDemo;

/**
 * Урок 2.7 — шаблоны проектирования и безопасный код.
 */
public class App {

    public static void main(String[] args) {
        pattern1Singleton();
        pattern2ObjectPool();
        pattern3FactoryMethod();
        pattern4AbstractFactory();
        pattern5Prototype();
        pattern6Builder();
        pattern7Proxy();
        pattern8Adapter();
        pattern9Decorator();
        pattern10Composite();
        pattern11Bridge();
        pattern12Facade();
        pattern13Flyweight();
        pattern14ChainOfResponsibility();
        pattern15Command();
        pattern16Interpreter();
        pattern17Iterator();
        pattern18Mediator();
        pattern19Memento();
        pattern20Observer();
        pattern21State();
        pattern22Strategy();
        pattern23NullObject();
        pattern24TemplateMethod();
        pattern25Visitor();
    }

    static void pattern1Singleton() {
        printPattern("Singleton");
        SingletonDemo.run();
    }

    static void pattern2ObjectPool() {
        printPattern("Object Pool");
        ObjectPoolDemo.run();
    }

    static void pattern3FactoryMethod() {
        printPattern("Factory Method");
        FactoryMethodDemo.run();
    }

    static void pattern4AbstractFactory() {
        printPattern("Abstract Factory");
        AbstractFactoryDemo.run();
    }

    static void pattern5Prototype() {
        printPattern("Prototype");
        PrototypeDemo.run();
    }

    static void pattern6Builder() {
        printPattern("Builder");
        BuilderDemo.run();
    }

    static void pattern7Proxy() {
        printPattern("Proxy");
        ProxyDemo.run();
    }

    static void pattern8Adapter() {
        printPattern("Adapter");
        AdapterDemo.run();
    }

    static void pattern9Decorator() {
        printPattern("Decorator");
        DecoratorDemo.run();
    }

    static void pattern10Composite() {
        printPattern("Composite");
        CompositeDemo.run();
    }

    static void pattern11Bridge() {
        printPattern("Bridge");
        BridgeDemo.run();
    }

    static void pattern12Facade() {
        printPattern("Facade");
        FacadeDemo.run();
    }

    static void pattern13Flyweight() {
        printPattern("Flyweight");
        FlyweightDemo.run();
    }

    static void pattern14ChainOfResponsibility() {
        printPattern("Chain of Responsibility");
        ChainOfResponsibilityDemo.run();
    }

    static void pattern15Command() {
        printPattern("Command");
        CommandDemo.run();
    }

    static void pattern16Interpreter() {
        printPattern("Interpreter");
        InterpreterDemo.run();
    }

    static void pattern17Iterator() {
        printPattern("Iterator");
        IteratorDemo.run();
    }

    static void pattern18Mediator() {
        printPattern("Mediator");
        MediatorDemo.run();
    }

    static void pattern19Memento() {
        printPattern("Memento");
        MementoDemo.run();
    }

    static void pattern20Observer() {
        printPattern("Observer");
        ObserverDemo.run();
    }

    static void pattern21State() {
        printPattern("State");
        StateDemo.run();
    }

    static void pattern22Strategy() {
        printPattern("Strategy");
        StrategyDemo.run();
    }

    static void pattern23NullObject() {
        printPattern("Null Object");
        NullObjectDemo.run();
    }

    static void pattern24TemplateMethod() {
        printPattern("Template Method");
        TemplateMethodDemo.run();
    }

    static void pattern25Visitor() {
        printPattern("Visitor");
        VisitorDemo.run();
    }

    static void printPattern(String name) {
        System.out.println("\n--- " + name + " ---");
    }
}
